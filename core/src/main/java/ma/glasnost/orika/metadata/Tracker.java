package ma.glasnost.orika.metadata;

import java.util.HashSet;

public class Tracker {

	private HashSet<TypePropertyKey> tracked = new HashSet<TypePropertyKey>();

	public void track(Property p, Type<?> owner) {
		TypePropertyKey e = new TypePropertyKey(p, owner);
		tracked.add(e);
	}

	public boolean has(Property p, Type<?> owner) {
		TypePropertyKey e = new TypePropertyKey(p, owner);
		return tracked.contains(e);
	}

	public static class TypePropertyKey {

		private String name;
		private Type<?> owner;
		private Type<?> type;

		/**
		 * @param name
		 * @param owner
		 * @param type
		 */
		public TypePropertyKey(Property property, Type<?> owner) {
			super();
			this.name = property.getName();
			this.type = property.getType();
			this.owner = owner;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((owner == null) ? 0 : owner.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TypePropertyKey other = (TypePropertyKey) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (owner == null) {
				if (other.owner != null)
					return false;
			} else if (!owner.equals(other.owner))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}
	}
	
	private static ThreadLocal<Tracker> current = new ThreadLocal<Tracker>();

	public static Tracker current() {
		if(current.get() == null) {
			current.set(new Tracker());
		}
		return current.get();
	}
}
