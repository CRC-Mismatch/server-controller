package com.cerner.fsi.scp.control;

import com.cerner.fsi.scp.control.user.SCPUserInfo;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static java.util.Optional.*;

public class Authenticator
{

   private static final Logger logger = LoggerFactory.getLogger(Authenticator.class);
   private static final int CONNECTION_TIME_OUT = 3000;

   public boolean hasAccessToDomain(String hostName, String userName, String password)
   {
      boolean hasAccess = false;

      if (stringParametersEmptyOrNull(hostName, userName, password))
         return hasAccess;

      try
      {
         final Session session = connectToHost(hostName, userName, password)
               .orElseThrow(
                     ()-> new JSchException("Unable to login to backend host :" + hostName)
               );
         hasAccess = session.isConnected();
         session.disconnect();
      }
      catch (JSchException e)
      {
         logger.error("SSH connection failed {}", e);
         return false;
      }
      logger.info("Access to the backend host {} : {}", hostName, hasAccess);
      return hasAccess;
   }

   private boolean stringParametersEmptyOrNull(String hostName, String userName, String password)
   {
      if (StringUtils.stripToNull(hostName) == null || StringUtils.stripToNull(userName) == null
            || StringUtils.stripToNull(password) == null)
         return true;
      return false;
   }

   Optional<Session> connectToHost(String domain, String userName, String password) throws JSchException
   {
      logger.debug("Attempting to establish the connection to the host {}", domain);
      final Optional<Session> sshSession = ofNullable(new JSch().getSession(userName, domain));
      if(sshSession.isPresent()){
         logger.debug("Session to the backend established. Connecting to the ssh session...");
         sshSession.get().setPassword(password);
         sshSession.get().setConfig("StrictHostKeyChecking", "no");
         sshSession.get().connect(CONNECTION_TIME_OUT);
      }
      return sshSession;
   }

   Optional<SCPUserInfo> constructFrontEndAccess(String frontEndUserName, String frontEndPassword, String frontEndHostname)
   {
      if(stringParametersEmptyOrNull(frontEndHostname, frontEndUserName, frontEndPassword)){
         logger.trace("Illegal parameters. frontend hostname : {}, frontend username : {} or invalid password", frontEndHostname, frontEndUserName);
         return empty();
      }
      return of(new SCPUserInfo(frontEndUserName, frontEndPassword, frontEndHostname));
   }
}
