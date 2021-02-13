package ru.job4j.jsonDto;

import java.util.Set;

public class UuidJson {
	private Set<String> uuids;

	public UuidJson(Set<String> uuids) {
		this.uuids = uuids;
	}

	public Set<String> getUuids() {
		return uuids;
	}

	public void setUuids(Set<String> uuids) {
		this.uuids = uuids;
	}

	@Override
	public String toString() {
		return "UuidJson [uuids=" + uuids + "]";
	}
}
