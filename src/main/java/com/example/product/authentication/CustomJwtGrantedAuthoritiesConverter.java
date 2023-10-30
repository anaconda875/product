package com.example.product.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
  private final Log logger = LogFactory.getLog(getClass());

  private static final String DEFAULT_AUTHORITY_PREFIX = "SCOPE_";

  private static final Collection<String> WELL_KNOWN_AUTHORITIES_CLAIM_NAMES = Arrays.asList("scope", "scp");

  private String authorityPrefix = DEFAULT_AUTHORITY_PREFIX;

  private String authoritiesClaimName;

  /**
   * Extract {@link GrantedAuthority}s from the given {@link Jwt}.
   * @param jwt The {@link Jwt} token
   * @return The {@link GrantedAuthority authorities} read from the token scopes
   */
  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    for (String authority : getAuthorities(jwt)) {
      grantedAuthorities.add(new SimpleGrantedAuthority(this.authorityPrefix + authority));
    }
    return grantedAuthorities;
  }

  public void setAuthorityPrefix(String authorityPrefix) {
    Assert.notNull(authorityPrefix, "authorityPrefix cannot be null");
    this.authorityPrefix = authorityPrefix;
  }

  public void setAuthoritiesClaimName(String authoritiesClaimName) {
    Assert.hasText(authoritiesClaimName, "authoritiesClaimName cannot be empty");
    this.authoritiesClaimName = authoritiesClaimName;
  }

  private String getAuthoritiesClaimName(Jwt jwt) {
    if (this.authoritiesClaimName != null) {
      return this.authoritiesClaimName;
    }
    for (String claimName : WELL_KNOWN_AUTHORITIES_CLAIM_NAMES) {
      if (jwt.hasClaim(claimName)) {
        return claimName;
      }
    }
    return null;
  }

  private Collection<String> getAuthorities(Jwt jwt) {
    String claimName = getAuthoritiesClaimName(jwt);
    if (claimName == null) {
      this.logger.trace("Returning no authorities since could not find any claims that might contain scopes");
      return Collections.emptyList();
    }
    if (this.logger.isTraceEnabled()) {
      this.logger.trace(LogMessage.format("Looking for scopes in claim %s", claimName));
    }
    Object authorities = jwt.getClaim(claimName);

    Collection<String> result = new ArrayList<>();
    List<String> roles = extractResourceAccess(jwt, new String[] {"product"});

    if (authorities instanceof String) {
      if (StringUtils.hasText((String) authorities)) {
//        return Arrays.asList(((String) authorities).split(" "));
        result.addAll(List.of(((String) authorities).split(" ")));
        result.addAll(roles);
      }
//      return Collections.emptyList();
    } else if (authorities instanceof Collection) {
      result = castAuthoritiesToCollection(authorities);
      result.addAll(roles);
    }

    return result;
  }

  private List<String> extractResourceAccess(Jwt jwt, String[] arr) {
    List<String> result = new ArrayList<>();
    Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
    for(String k : arr) {
      Map<String, Object> map = (HashMap<String, Object>) resourceAccess.get(k);
      List<String> roles = (List<String>) map.get("roles");
      result.addAll(roles);
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  private Collection<String> castAuthoritiesToCollection(Object authorities) {
    return (Collection<String>) authorities;
  }
}
