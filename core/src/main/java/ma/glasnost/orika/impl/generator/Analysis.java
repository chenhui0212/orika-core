package ma.glasnost.orika.impl.generator;

import java.lang.reflect.Modifier;

class Analysis {

	static Visibility getMostRestrictiveVisibility(Class<?> classToCheck) {
		Visibility visibility = Visibility.PUBLIC;
		Class<?> currentClass = classToCheck;
		while (currentClass != null) {
			int modifiers = currentClass.getModifiers();
			if (Modifier.isPrivate(modifiers)) {
				visibility = Visibility.PRIVATE;
			} else if (Modifier.isProtected(modifiers) && visibility != Visibility.PRIVATE) {
				visibility = Visibility.PROTECTED;
			} else if (Modifier.isPublic(modifiers)) {
				// visibility = Visibility.PUBLIC not needed because if visibiliy were anything
				// else than PUBLIC we wouldn't set it anyways
			} else if (visibility != Visibility.PRIVATE && visibility != Visibility.PROTECTED) {
				visibility = Visibility.PACKAGE;
			}
			currentClass = currentClass.getEnclosingClass();
		}
		return visibility;
	}
	
	enum Visibility {
		PRIVATE, PACKAGE, PROTECTED, PUBLIC
	}
	
	private Analysis() {
		throw new UnsupportedOperationException("not instantiable");
	}
}
