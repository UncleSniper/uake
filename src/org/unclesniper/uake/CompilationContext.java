package org.unclesniper.uake;

import java.util.Deque;
import java.util.LinkedList;
import java.util.IdentityHashMap;
import org.unclesniper.uake.syntax.SoftCall;
import org.unclesniper.uake.syntax.Parameter;
import org.unclesniper.uake.semantics.UakeType;
import org.unclesniper.uake.semantics.Property;
import org.unclesniper.uake.semantics.Provision;
import org.unclesniper.uake.semantics.UakeScope;
import org.unclesniper.uake.semantics.Transform;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.semantics.UakeScoped;
import org.unclesniper.uake.syntax.QualifiedName;
import org.unclesniper.uake.syntax.TypeDefinition;
import org.unclesniper.uake.semantics.UakeFunction;
import org.unclesniper.uake.semantics.UakeVariable;
import org.unclesniper.uake.syntax.ModuleDefinition;
import org.unclesniper.uake.syntax.BindingExpression;
import org.unclesniper.uake.syntax.FunctionDefinition;
import org.unclesniper.uake.syntax.VariableDefinition;
import org.unclesniper.uake.syntax.PropertyDefinition;
import org.unclesniper.uake.semantics.UakeTypeTemplate;
import org.unclesniper.uake.syntax.ProvisionDefinition;
import org.unclesniper.uake.semantics.ProvisionTemplate;
import org.unclesniper.uake.semantics.InstanceofTransform;
import org.unclesniper.uake.semantics.UakeFunctionTemplate;

public class CompilationContext {

	private static final Transform<UakeScoped, UakeScope> SCOPED_TO_SCOPE
			= new InstanceofTransform<UakeScoped, UakeScope>(UakeScope.class);

	private final DefinitionContext definitionContext;

	private UakeModule targetModule;

	private final IdentityHashMap<ModuleDefinition, UakeModule> modulesByDefinition
			= new IdentityHashMap<ModuleDefinition, UakeModule>();

	private final IdentityHashMap<FunctionDefinition, UakeFunction> functionsByDefinition
			= new IdentityHashMap<FunctionDefinition, UakeFunction>();

	private final IdentityHashMap<FunctionDefinition, UakeFunctionTemplate> functionTemplatesByDefinition
			= new IdentityHashMap<FunctionDefinition, UakeFunctionTemplate>();

	private final IdentityHashMap<VariableDefinition, UakeVariable> variablesByDefinition
			= new IdentityHashMap<VariableDefinition, UakeVariable>();

	private final IdentityHashMap<TypeDefinition, UakeType> typesByDefinition
			= new IdentityHashMap<TypeDefinition, UakeType>();

	private final IdentityHashMap<TypeDefinition, UakeTypeTemplate> typeTemplatesByDefinition
			= new IdentityHashMap<TypeDefinition, UakeTypeTemplate>();

	private final IdentityHashMap<PropertyDefinition, Property> propertiesByDefinition
			= new IdentityHashMap<PropertyDefinition, Property>();

	private final IdentityHashMap<ProvisionDefinition, Provision> provisionsByDefinition
			= new IdentityHashMap<ProvisionDefinition, Provision>();

	private final IdentityHashMap<ProvisionDefinition, ProvisionTemplate> provisionTemplatesByDefinition
			= new IdentityHashMap<ProvisionDefinition, ProvisionTemplate>();

	private final IdentityHashMap<BindingExpression, UakeVariable> variablesByBindingExpression
			= new IdentityHashMap<BindingExpression, UakeVariable>();

	private final IdentityHashMap<BindingExpression, UakeScope> scopesByBindingExpression
			= new IdentityHashMap<BindingExpression, UakeScope>();

	private final IdentityHashMap<SoftCall, UakeModule> modulesBySoftCall
			= new IdentityHashMap<SoftCall, UakeModule>();

	private final IdentityHashMap<Parameter, UakeVariable> variablesByParameter
			= new IdentityHashMap<Parameter, UakeVariable>();

	private final Deque<UakeScope> scope = new LinkedList<UakeScope>();

	public CompilationContext(DefinitionContext definitionContext) {
		this.definitionContext = definitionContext;
		targetModule = definitionContext.getRootModule();
		pushScope(targetModule);
	}

	public DefinitionContext getDefinitionContext() {
		return definitionContext;
	}

	public UakeModule getTargetModule() {
		return targetModule;
	}

	public UakeModule setTargetModule(UakeModule targetModule) {
		UakeModule old = this.targetModule;
		this.targetModule = targetModule;
		return old;
	}

	public UakeModule getModuleByDefinition(ModuleDefinition definition) {
		return modulesByDefinition.get(definition);
	}

	public void putModuleForDefinition(ModuleDefinition definition, UakeModule module) {
		modulesByDefinition.put(definition, module);
	}

	public UakeFunction getFunctionByDefinition(FunctionDefinition definition) {
		return functionsByDefinition.get(definition);
	}

	public void putFunctionForDefinition(FunctionDefinition definition, UakeFunction function) {
		functionsByDefinition.put(definition, function);
	}

	public UakeFunctionTemplate getFunctionTemplateByDefinition(FunctionDefinition definition) {
		return functionTemplatesByDefinition.get(definition);
	}

	public void putFunctionTemplateForDefinition(FunctionDefinition definition, UakeFunctionTemplate function) {
		functionTemplatesByDefinition.put(definition, function);
	}

	public UakeVariable getVariableByDefinition(VariableDefinition definition) {
		return variablesByDefinition.get(definition);
	}

	public void putVariableForDefinition(VariableDefinition definition, UakeVariable variable) {
		variablesByDefinition.put(definition, variable);
	}

	public UakeType getTypeByDefinition(TypeDefinition definition) {
		return typesByDefinition.get(definition);
	}

	public void putTypeForDefinition(TypeDefinition definition, UakeType type) {
		typesByDefinition.put(definition, type);
	}

	public UakeTypeTemplate getTypeTemplateByDefinition(TypeDefinition definition) {
		return typeTemplatesByDefinition.get(definition);
	}

	public void putTypeTemplateForDefinition(TypeDefinition definition, UakeTypeTemplate type) {
		typeTemplatesByDefinition.put(definition, type);
	}

	public Property getPropertyByDefinition(PropertyDefinition definition) {
		return propertiesByDefinition.get(definition);
	}

	public void putPropertyForDefinition(PropertyDefinition definition, Property property) {
		propertiesByDefinition.put(definition, property);
	}

	public Provision getProvisionByDefinition(ProvisionDefinition definition) {
		return provisionsByDefinition.get(definition);
	}

	public void putProvisionForDefinition(ProvisionDefinition definition, Provision provision) {
		provisionsByDefinition.put(definition, provision);
	}

	public ProvisionTemplate getProvisionTemplateByDefinition(ProvisionDefinition definition) {
		return provisionTemplatesByDefinition.get(definition);
	}

	public void putProvisionTemplateForDefinition(ProvisionDefinition definition, ProvisionTemplate provision) {
		provisionTemplatesByDefinition.put(definition, provision);
	}

	public UakeVariable getVariableByBindingExpression(BindingExpression expression) {
		return variablesByBindingExpression.get(expression);
	}

	public void putVariableForBindingExpression(BindingExpression expression, UakeVariable variable) {
		variablesByBindingExpression.put(expression, variable);
	}

	public UakeScope getScopeByBindingExpression(BindingExpression expression) {
		return scopesByBindingExpression.get(expression);
	}

	public void putScopeForBindingExpression(BindingExpression expression, UakeScope scope) {
		scopesByBindingExpression.put(expression, scope);
	}

	public UakeModule getModuleBySoftCall(SoftCall call) {
		return modulesBySoftCall.get(call);
	}

	public void putModuleForSoftCall(SoftCall call, UakeModule module) {
		modulesBySoftCall.put(call, module);
	}

	public UakeVariable getVariableByParameter(Parameter parameter) {
		return variablesByParameter.get(parameter);
	}

	public void putVariableForParameter(Parameter parameter, UakeVariable variable) {
		variablesByParameter.put(parameter, variable);
	}

	public void pushScope(UakeScope scope) {
		if(scope == null)
			throw new NullPointerException("Cannot push null scope");
		this.scope.addFirst(scope);
	}

	public void popScope() {
		scope.removeFirst();
	}

	public UakeModule.Group<UakeScoped> resolveName(String name) {
		UakeModule.Group<UakeScoped> selection = new UakeModule.Group<UakeScoped>();
		if(!scope.isEmpty())
			scope.getFirst().resolve(name, selection);
		return selection;
	}

	public UakeModule.Group<UakeScoped> resolveNameFail(String name, Location referenceLocation) {
		UakeModule.Group<UakeScoped> selection = resolveName(name);
		if(selection.isEmpty())
			throw new UnboundNameException(referenceLocation, name);
		return selection;
	}

	public UakeModule.Group<UakeScoped> resolveName(QualifiedName name) {
		return resolveName(name, false);
	}

	public UakeModule.Group<UakeScoped> resolveNameFail(QualifiedName name) {
		return resolveName(name, true);
	}

	public UakeModule.Group<UakeScoped> resolveName(QualifiedName name, boolean fail) {
		UakeModule.Group<UakeScoped> selection = null;
		for(QualifiedName.Segment segment : name.getSegments()) {
			UakeModule.Group<UakeScoped> next = new UakeModule.Group<UakeScoped>();
			if(selection == null) {
				for(UakeScope level : scope)
					level.resolve(segment.getName(), next);
			}
			else {
				for(UakeScope enclosing : selection.map(CompilationContext.SCOPED_TO_SCOPE))
					enclosing.resolve(segment.getName(), next);
			}
			selection = next;
			if(selection.isEmpty()) {
				if(fail)
					throw new UnboundNameException(segment.getLocation(), segment.getName());
				else
					break;
			}
		}
		return selection;
	}

}
