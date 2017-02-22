package com.cerner.fsi.scp.control.user;

/**
 * Created by VR033549 on 10/6/2016.
 */
public final class SCPUserInfo
{
   private final String userName;
   private final String password;
   private final String hostname;

   public String getUserName()
   {
      return userName;
   }

   public String getPassword()
   {
      return password;
   }

   public String getHostname()
   {
      return hostname;
   }

   public SCPUserInfo(String userName, String password, String hostname)
   {
      this.userName = userName;
      this.password = password;
      this.hostname = hostname;
   }
}
