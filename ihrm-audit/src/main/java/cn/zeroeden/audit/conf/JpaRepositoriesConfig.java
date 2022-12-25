package cn.zeroeden.audit.conf;

//@Configuration
//@EnableJpaRepositories(
//	basePackages = "com.ihrm.audit.dao",
//	entityManagerFactoryRef = "ihrmEntityManager",
//	transactionManagerRef = "ihrmTransactionManager"
//)
public class JpaRepositoriesConfig {
//
//	@Autowired
//	private Environment env;
//
//	@Autowired
//	@Qualifier("ihrmDataSource")
//	private DataSource ihrmDataSource;
//
//	@Bean
//	@Primary
//	public LocalContainerEntityManagerFactoryBean ihrmEntityManager() {
//		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//		em.setDataSource(ihrmDataSource);
//		em.setPackagesToScan(new String[] { "com.ihrm.audit.entity" });
//		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//		em.setJpaVendorAdapter(vendorAdapter);
//		HashMap<String, Object> properties = new HashMap<>();
//		properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//		properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
//		em.setJpaPropertyMap(properties);
//		return em;
//	}
//
//	@Primary
//	@Bean
//	public PlatformTransactionManager ihrmTransactionManager() {
//		JpaTransactionManager transactionManager = new JpaTransactionManager();
//		transactionManager.setEntityManagerFactory( ihrmEntityManager().getObject());
//		return transactionManager;
//	}
}
