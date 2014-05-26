<?php
	//include all DAO files
	require_once('class/sql/Connection.class.php');
	require_once('class/sql/ConnectionFactory.class.php');
	require_once('class/sql/ConnectionProperty.class.php');
	require_once('class/sql/QueryExecutor.class.php');
	require_once('class/sql/Transaction.class.php');
	require_once('class/sql/SqlQuery.class.php');
	require_once('class/core/ArrayList.class.php');
	require_once('class/dao/DAOFactory.class.php');
 	
	require_once('class/dao/PersonalDbLanguagePrefDAO.class.php');
	require_once('class/dto/PersonalDbLanguagePref.class.php');
	require_once('class/mysql/PersonalDbLanguagePrefMySqlDAO.class.php');
	require_once('class/mysql/ext/PersonalDbLanguagePrefMySqlExtDAO.class.php');
	require_once('class/dao/PersonalDbUserDetailsDAO.class.php');
	require_once('class/dto/PersonalDbUserDetail.class.php');
	require_once('class/mysql/PersonalDbUserDetailsMySqlDAO.class.php');
	require_once('class/mysql/ext/PersonalDbUserDetailsMySqlExtDAO.class.php');
	require_once('class/dao/RoleUserDAO.class.php');
	require_once('class/dto/RoleUser.class.php');
	require_once('class/mysql/RoleUserMySqlDAO.class.php');
	require_once('class/mysql/ext/RoleUserMySqlExtDAO.class.php');
	require_once('class/dao/RolesDAO.class.php');
	require_once('class/dto/Role.class.php');
	require_once('class/mysql/RolesMySqlDAO.class.php');
	require_once('class/mysql/ext/RolesMySqlExtDAO.class.php');
	require_once('class/dao/UserDbDAO.class.php');
	require_once('class/dto/UserDb.class.php');
	require_once('class/mysql/UserDbMySqlDAO.class.php');
	require_once('class/mysql/ext/UserDbMySqlExtDAO.class.php');
	require_once('class/dao/UserPerformanceDAO.class.php');
	require_once('class/dto/UserPerformance.class.php');
	require_once('class/mysql/UserPerformanceMySqlDAO.class.php');
	require_once('class/mysql/ext/UserPerformanceMySqlExtDAO.class.php');

?>