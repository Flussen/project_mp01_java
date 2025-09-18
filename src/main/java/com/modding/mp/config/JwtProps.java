package com.modding.mp.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtProps {
  private String issuer;
  private String audience;
  private Access access = new Access();
  private Refresh refresh = new Refresh();

  public static class Access {
    private Duration ttl = Duration.ofMinutes(10);
    public Duration getTtl() { return ttl; }
    public void setTtl(Duration ttl) { this.ttl = ttl; }
  }
  public static class Refresh {
    private Duration ttl = Duration.ofDays(30);
    private Duration absoluteTtl = Duration.ofDays(180);
    public Duration getTtl() { return ttl; }
    public void setTtl(Duration ttl) { this.ttl = ttl; }
    public Duration getAbsoluteTtl() { return absoluteTtl; }
    public void setAbsoluteTtl(Duration absoluteTtl) { this.absoluteTtl = absoluteTtl; }
  }

  public String getIssuer() { return issuer; }
  public void setIssuer(String issuer) { this.issuer = issuer; }
  public String getAudience() { return audience; }
  public void setAudience(String audience) { this.audience = audience; }
  public Access getAccess() { return access; }
  public Refresh getRefresh() { return refresh; }
}
