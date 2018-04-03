package org.unclesniper.uake;

import java.util.IdentityHashMap;
import org.unclesniper.uake.semantics.UakeType;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.syntax.TypeDefinition;
import org.unclesniper.uake.semantics.UakeFunction;
import org.unclesniper.uake.semantics.UakeVariable;
import org.unclesniper.uake.syntax.ModuleDefinition;
import org.unclesniper.uake.syntax.FunctionDefinition;
import org.unclesniper.uake.syntax.VariableDefinition;
import org.unclesniper.uake.semantics.UakeTypeTemplate;
import org.unclesniper.uake.semantics.UakeFunctionTemplate;

public class CompilationContext {

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

	public CompilationContext(DefinitionContext definitionContext) {
		this.definitionContext = definitionContext;
		targetModule = definitionContext.getRootModule();
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

}
