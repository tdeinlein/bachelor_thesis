/*
* generated by Xtext
*/
package org.xtext.orcasdk.entitymodel;

import org.eclipse.xtext.junit4.IInjectorProvider;

import com.google.inject.Injector;

public class EntityModelUiInjectorProvider implements IInjectorProvider {
	
	public Injector getInjector() {
		return org.xtext.orcasdk.entitymodel.ui.internal.EntityModelActivator.getInstance().getInjector("org.xtext.orcasdk.entitymodel.EntityModel");
	}
	
}
