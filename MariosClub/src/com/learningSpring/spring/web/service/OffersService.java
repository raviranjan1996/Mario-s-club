package com.learningSpring.spring.web.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.learningSpring.spring.web.dao.Offer;
import com.learningSpring.spring.web.dao.OffersDAO;

@Service("offersService")
public class OffersService {
	
	private OffersDAO offersDao;

	@Autowired
	public void setOffersDao(OffersDAO offersDao) {
		this.offersDao = offersDao;
	}
	
	
	public List<Offer> getCurrent(){
		
		return offersDao.getOffers();
	}

	
	@Secured({"ROLE_ADMIN" , "ROLE_USER"})
	public void create(Offer offer) {
		
		offersDao.saveOrUpdate(offer);
		
	}


	public boolean hasOffer(String name) {
		

		if(name == null) {
			return false;
		}
		
		List<Offer> offers = offersDao.getOffers(name);
		
		if(offers.size() == 0) {
			return false;
		}
		
		return true;
	}


	public Offer getOffer(String username) {
		
		if(username == null)
		{
			return null;
		}
		
		List<Offer> offers = offersDao.getOffers(username);
		if(offers.size() == 0)
		{
			return null;
		}
		
		return offers.get(0);
	}


	public void saveOrUpdate(@Valid Offer offer) {
			offersDao.saveOrUpdate(offer);
	}


	public void deleteOffer(int id) {
	
		offersDao.delete(id);
		
	}
}
