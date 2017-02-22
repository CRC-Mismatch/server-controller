package com.cerner.fsi.scp.control;

import com.cerner.fsi.scp.categories.IntegrationTests;
import com.cerner.fsi.scp.control.user.SCPUserInfo;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.NoSuchElementException;
import java.util.Optional;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class AuthenticatorTest
{
   private Authenticator authenticator;
   private String domain;
   private String userName;
   private String password;

   @Before
   public void setUp(){
      authenticator = new Authenticator();
      domain = "mockDomain";
      userName = "user";
      password = "password";
   }

   @Test
   public void testCanary(){
      assertTrue(true);
   }

   @Test
   public void testHasAccessToADomainNameWithDomainBeingNull() throws JSchException
   {
      domain = null;
      assertFalse("False expected due to domain being a null.", authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   public void testHasAccessToADomainNameWithDomainBeingEmpty() throws JSchException
   {
      domain = "";
      assertFalse("False expected due to domain being an empty string.", authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   public void testHasAccessToADomainNameWithUserNameBeingNull() throws JSchException
   {
      userName = null;
      assertFalse("False expected due to userName being an empty string.", authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   public void testHasAccessToAUserNameWithDomainBeingEmpty() throws JSchException
   {
      userName = "";
      assertFalse("False expected due to userName being an empty string.", authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   public void testHasAccessToADomainNameWithPasswordBeingNull() throws JSchException
   {
      password = null;
      assertFalse("False expected due to password being an empty string.", authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   public void testHasAccessToAUserNameWithPasswordBeingEmpty() throws JSchException
   {
      password = "";
      assertFalse("False expected due to password being an empty string.", authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   @Category(IntegrationTests.class)
   public void testPerformAuthenticationWithTheCredentials_Integration_Success() throws JSchException
   {
      domain = "ip1201ar";
      userName = "vr033549";
      password = "Compass2014";
      assertTrue(authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   public void testPerformAuthenticationWithTheUserNameBeingAnInvalidOne() throws JSchException
   {
      domain = "invalid_domain";
      userName = "mockUsername";
      password = "mockPassword";

      authenticator = new Authenticator(){
         @Override Optional<Session> connectToHost(String domain, String userName, String password) throws JSchException
         {
            throw new JSchException();
         }
      };

      assertFalse("Expected connectToHost to throw an exception and hasAccessToDomain to return a false.",
            authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   public void testPerformAuthenticationWhenTheConnectToReturnsAnEmptyObject() throws JSchException
   {
      domain = "mockDomain";
      userName = "mockUsername";
      password = "mockPassword";

      authenticator = new Authenticator(){
         @Override Optional<Session> connectToHost(String domain, String userName, String password) throws JSchException
         {
            return Optional.empty();
         }
      };

      assertFalse("Expected connectToHost to return a false.",
            authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   public void testPerformAuthenticationSuccessWithAllValidCredentials() throws JSchException
   {

      final Session session = mock(Session.class);
      when(session.isConnected()).thenReturn(true);
      doNothing().when(session).disconnect();

      authenticator = new Authenticator(){
         @Override Optional<Session> connectToHost(String domain, String userName, String password) throws JSchException
         {
            return Optional.of(session);
         }
      };

      assertTrue(authenticator.hasAccessToDomain(domain, userName, password));
   }

   @Test
   public void testTakeInFrontEndCredentialsAndConstructASession(){

      String frontEndUserName = "mockUser";
      String frontEndPassword = "mockPassword";
      String frontEndHostname = "mockHostName";
      SCPUserInfo userInfo = authenticator.constructFrontEndAccess(frontEndUserName, frontEndPassword, frontEndHostname).get();

      assertEquals(frontEndHostname, userInfo.getHostname());
      assertEquals(frontEndUserName, userInfo.getUserName());
      assertEquals(frontEndPassword, userInfo.getPassword());

   }

   @Test(expected = NoSuchElementException.class)
   public void testTakeInFrontEndCredentialsWithInvalidHostName(){
      String frontEndUserName = "";
      String frontEndPassword = "mockPassword";
      String frontEndHostname = "mockHostName";
      authenticator.constructFrontEndAccess(frontEndUserName, frontEndPassword, frontEndHostname).get();
   }

   @Test(expected = NoSuchElementException.class)
   public void testTakeInFrontEndCredentialsWithInvalidPassword(){
      String frontEndUserName = "mockUsername";
      String frontEndPassword = "";
      String frontEndHostname = "mockHostName";
      authenticator.constructFrontEndAccess(frontEndUserName, frontEndPassword, frontEndHostname).get();
   }

   @Test(expected = NoSuchElementException.class)
   public void testTakeInFrontEndCredentialsWithInvalidHostname(){
      String frontEndUserName = "mockUsername";
      String frontEndPassword = "mockPassword";
      String frontEndHostname = null;
      authenticator.constructFrontEndAccess(frontEndUserName, frontEndPassword, frontEndHostname).get();
   }

}
