package org.unclesniper.uake;

import java.util.IdentityHashMap;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.semantics.UakeFunction;
import org.unclesniper.uake.syntax.ModuleDefinition;
import org.unclesniper.uake.syntax.FunctionDefinition;
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

}
