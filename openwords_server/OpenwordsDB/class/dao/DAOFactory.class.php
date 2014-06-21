<?php

/**
 * DAOFactory
 * @author: http://phpdao.com
 * @date: ${date}
 */
class DAOFactory{
	
	/**
	 * @return PersonalDbLanguagePrefDAO
	 */
	public static function getPersonalDbLanguagePrefDAO(){
		return new PersonalDbLanguagePrefMySqlExtDAO();
	}

	/**
	 * @return PersonalDbUserDetailsDAO
	 */
	public static function getPersonalDbUserDetailsDAO(){
		return new PersonalDbUserDetailsMySqlExtDAO();
	}

	/**
	 * @return RoleUserDAO
	 */
	public static function getRoleUserDAO(){
		return new RoleUserMySqlExtDAO();
	}

	/**
	 * @return RolesDAO
	 */
	public static function getRolesDAO(){
		return new RolesMySqlExtDAO();
	}

	/**
	 * @return UserDbDAO
	 */
	public static function getUserDbDAO(){
		return new UserDbMySqlExtDAO();
	}

	/**
	 * @return UserPerformanceDAO
	 */
	public static function getUserPerformanceDAO(){
		return new UserPerformanceMySqlExtDAO();
	}


}
?>