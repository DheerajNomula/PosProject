package com.increff.employee.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.UserDao;
import com.increff.employee.pojo.UserPojo;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional(rollbackFor = ApiException.class)
	public void add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		dao.insert(p);
	}

	@Transactional(rollbackFor = ApiException.class,readOnly = true)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional(readOnly = true)
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
		p.setRole(p.getRole().toLowerCase().trim());
	}
}
