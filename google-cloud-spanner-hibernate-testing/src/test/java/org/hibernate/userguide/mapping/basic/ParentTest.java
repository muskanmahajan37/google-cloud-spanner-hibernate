/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.userguide.mapping.basic;

import org.hibernate.annotations.Parent;
import org.hibernate.annotations.Target;
import org.hibernate.jpa.test.BaseEntityManagerFunctionalTestCase;
import org.junit.Test;

import javax.persistence.*;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.Assert.assertSame;

/**
 * @author Vlad Mihalcea
 */
public class ParentTest extends BaseEntityManagerFunctionalTestCase {

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
			City.class,
		};
	}

	@Test
	public void testLifecycle() {
		//tag::mapping-Parent-persist-example[]
		doInJPA( this::entityManagerFactory, entityManager -> {

			City cluj = new City();
			cluj.setId(666);
			cluj.setName( "Cluj" );
			cluj.setCoordinates( new GPS( 46.77120, 23.62360 ) );

			entityManager.persist( cluj );
		} );
		//end::mapping-Parent-persist-example[]


		//tag::mapping-Parent-fetching-example[]
		doInJPA( this::entityManagerFactory, entityManager -> {

			City cluj = entityManager.find( City.class, 666L );

			assertSame( cluj, cluj.getCoordinates().getCity() );
		} );
		//end::mapping-Parent-fetching-example[]
	}

	//tag::mapping-Parent-example[]

	@Embeddable
	public static class GPS {

		private double latitude;

		private double longitude;

		@Parent
		private City city;

		//Getters and setters omitted for brevity

	//end::mapping-Parent-example[]

		private GPS() {
		}

		public GPS(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public City getCity() {
			return city;
		}

		public void setCity(City city) {
			this.city = city;
		}
	//tag::mapping-Parent-example[]
	}
	//end::mapping-Parent-example[]

	//tag::mapping-Parent-example[]

	@Entity(name = "City_BasicParentTest")
	private static class City {

		@Id
		private Long id;

		private String name;

		@Embedded
		@Target( GPS.class )
		private GPS coordinates;

		//Getters and setters omitted for brevity

	//end::mapping-Parent-example[]
		
		public Long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public GPS getCoordinates() {
			return coordinates;
		}

		public void setCoordinates(GPS coordinates) {
			this.coordinates = coordinates;
		}
	//tag::mapping-Parent-example[]
	}
	//end::mapping-Parent-example[]
}
