package se.datasektionen.calypso.acl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AclConfig extends GlobalMethodSecurityConfiguration {

	@Bean
	protected MethodSecurityExpressionHandler expressionHandler() {
		var expressionHandler = new DefaultMethodSecurityExpressionHandler();

		// Enable custom permissions evaluator
		expressionHandler.setPermissionEvaluator(new CalypsoPermissionsEvaluator());

		return expressionHandler;
	}

}
