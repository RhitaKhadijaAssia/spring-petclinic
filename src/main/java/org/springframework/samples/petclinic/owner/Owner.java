package org.springframework.samples.petclinic.owner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.util.Assert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Simple JavaBean domain object representing an owner.
 */
@Entity
@Table(name = "owners")
public class Owner extends Person {

	@Column(name = "address")
	@NotBlank
	private String address = " ";

	@Column(name = "city")
	@NotBlank
	private String city = " ";

	@Column(name = "telephone")
	@NotBlank
	@Pattern(regexp = "\\d{10}", message = "{telephone.invalid}")
	private String telephone = " ";

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	@OrderBy("name")
	private final List<Pet> pets = new ArrayList<>();

	/* ===================== Getters & Setters ===================== */

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * Returns an unmodifiable view of pets to preserve encapsulation.
	 */
	public List<Pet> getPets() {
		return Collections.unmodifiableList(this.pets);
	}

	public void addPet(Pet pet) {
		Assert.notNull(pet, "Pet must not be null");
		if (pet.isNew()) {
			this.pets.add(pet);
		}
	}

	/* ===================== Pet Lookup ===================== */

	public Pet getPet(String name) {
		return getPet(name, false);
	}

	public Pet getPet(Integer id) {
		for (Pet pet : pets) {
			if (!pet.isNew() && Objects.equals(pet.getId(), id)) {
				return pet;
			}
		}
		return null;
	}

	public Pet getPet(String name, boolean ignoreNew) {
		for (Pet pet : pets) {
			if (pet.getName() != null
					&& pet.getName().equalsIgnoreCase(name)
					&& (!ignoreNew || !pet.isNew())) {
				return pet;
			}
		}
		return null;
	}

	/* ===================== Visits ===================== */

	/**
	 * Adds a visit to a pet.
	 */
	public void addVisit(Integer petId, Visit visit) {
		Assert.notNull(petId, "Pet identifier must not be null");
		Assert.notNull(visit, "Visit must not be null");

		Pet pet = getPet(petId);
		Assert.notNull(pet, "Invalid Pet identifier");

		pet.addVisit(visit);
	}

	/* ===================== Object Methods ===================== */

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", getId())
				.append("new", isNew())
				.append("lastName", getLastName())
				.append("firstName", getFirstName())
				.append("address", address)
				.append("city", city)
				.append("telephone", telephone)
				.toString();
	}
}
