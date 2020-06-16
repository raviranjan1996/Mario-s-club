package com.learningSpring.spring.web.dao;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Component("offersDao")
public class OffersDAO {

	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}

	@Autowired
	private SessionFactory sessionFactory;

	public Session session() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<Offer> getOffers() {
		Criteria crit = session().createCriteria(Offer.class);

		crit.createAlias("user", "u").add(Restrictions.eq("u.enabled", true));

		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Offer> getOffers(String username) {
		Criteria crit = session().createCriteria(Offer.class);

		crit.createAlias("user", "u");
		crit.add(Restrictions.eq("u.enabled", true));
		crit.add(Restrictions.eq("u.username", username));

		return crit.list();

	}

	@Transactional
	public int[] batchCreate(List<Offer> offer) {
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(offer.toArray());
		return jdbc.batchUpdate("insert into offers (id ,username , text) values (:id , :username , :text)", params);
	}

	
	public void saveOrUpdate(Offer offer) {

		session().saveOrUpdate(offer);
	}

	public boolean delete(int id) {
		Query query = session().createQuery("delete from Offer where id=:id");
		query.setLong("id", id);
		return query.executeUpdate() == 1;
	}

	public Offer getOffer(int id) {
		Criteria crit = session().createCriteria(Offer.class);

		crit.createAlias("user", "u");
		crit.add(Restrictions.eq("u.enabled", true));
		crit.add(Restrictions.idEq(id));

		return (Offer)crit.uniqueResult();
	}

}
