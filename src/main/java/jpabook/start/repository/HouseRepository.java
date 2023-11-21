package jpabook.start.repository;

import jpabook.start.domain.house.DateHouse;
import jpabook.start.domain.house.House;
import jpabook.start.domain.house.HouseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HouseRepository {

  public HouseRepository(EntityManager em) {
    this.em = em;
  }

  private EntityManager em;

  public void save(House house) { em.persist(house); }


  public House findOne(Long id) {
    return em.find(House.class, id);
  }

  public List<House> findByCapacity(int capacity) {
    return em.createQuery("select h from House h where h.capacity >= :capacity", House.class)
            .setParameter("capacity", capacity).getResultList();
  }

  public House findByName(String name) {
    try {
      return em.createQuery("select h from House h where h.name = :name", House.class)
              .setParameter("name", name)
              .getSingleResult();
    } catch (Exception e) {
      // 결과가 없는 경우 처리
      System.out.println("결과가 없는 경우");
    }
    return new House();
  }


  public List<House> findByType(HouseType houseType) {
    return em.createQuery("select h from House h where h.houseType = :houseType", House.class)
            .setParameter("houseType", houseType)
            .getResultList();
  }

  public List<House> findByDateRange(int checkIn, int checkOut) {
//    List<House> houses = em.createQuery("select h from House h join fetch h.dateHouses d where d.houseDate >= :checkIn and d.houseDate <= :checkOut", House.class)
//            .setParameter("checkIn", checkIn)
//            .setParameter("checkOut", checkOut)
//            .getResultList();
    List<House> houses = em.createQuery("select h from House h", House.class).getResultList();
    int count;
    List<House> returnHouse = new ArrayList<>();

    for (House house : houses) {
      count = 0;
      List<DateHouse> dateHouses = house.getDateHouses();
      for (DateHouse dateHouse : dateHouses) {
        if(dateHouse.getHouseDate() >= checkIn && dateHouse.getHouseDate() <= checkOut) {
          count++;
        }
      }
      if((checkOut - checkIn + 1) == count) {
        returnHouse.add(house);
      }
    }

    return houses;
  }


}
