package com.ezscrum.microservice.productbacklog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
/*
### disable this block comment to register with defined service discovery
@EnableEurekaClient
@EnableDiscoveryClient
@RibbonClients({
        @RibbonClient(name = "product_backlog", configuration = ProductbacklogApplication.UserConfig.class)
})*/
public class ProductbacklogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductbacklogApplication.class, args);
    }
/*
### disable this block comment to register with defined service discovery
    @Configuration
    static class UserConfig {

        private String name = "product_backlog";

        @Bean
        @ConditionalOnMissingBean
        public IClientConfig ribbonClientConfig() {
            DefaultClientConfigImpl config = new DefaultClientConfigImpl();
            config.loadProperties(this.name);
            return config;
        }

        @Bean
        ServerList<Server> ribbonServerList(IClientConfig config) {
            ConfigurationBasedServerList serverList = new ConfigurationBasedServerList();
            serverList.initWithNiwsConfig(config);
            return serverList;
        }

    }*/
}