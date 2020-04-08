import IntegrationModel, { IntegrationModelProperties } from './integration-js/src/integrationmodel';
import Parser from './integration-js/src/parser';
import Util from './integration-js/src/util';
import formulaIcon from '../icons/formula.png';
import chemIcon from '../icons/chem.png';

/**
 * Inits MathType creating an object with all properties that the IntegrationModel class
 * needs to initialize the plugin and create an instance of IntegrationModel child.
 * @param {HTMLElement} target - DOM target, in this integration the editable iframe.
 * @param {HTMLElement} toolbar - DOM element where icons will be inserted.
 */
window.wrs_int_init = function(target,toolbar) {
    /**
     * @type {integrationModelProperties}
     */
    var genericIntegrationProperties = {};
    genericIntegrationProperties.target = target;
    genericIntegrationProperties.toolbar = toolbar;

    // GenericIntegration instance.
    var genericIntegrationInstance = new GenericIntegration(genericIntegrationProperties);
    genericIntegrationInstance.init();
    genericIntegrationInstance.listeners.fire('onTargetReady', {});
}

/**
 * IntegrationModel constructor. This method sets the dependant
 * integration properties needed by the IntegrationModel class to init the plugin.
 */
export default class GenericIntegration extends IntegrationModel {

    /**
     * @constructs
     * @param {IntegrationModelProperties} integrationModelProperties
     */
    constructor(integrationModelProperties) {
        if (typeof(integrationModelProperties.serviceProviderProperties) === 'undefined') {
            integrationModelProperties.serviceProviderProperties = {};
            integrationModelProperties.serviceProviderProperties.URI = '/pluginwiris_engine/app/';
            integrationModelProperties.serviceProviderProperties.server = 'java';
        }
        integrationModelProperties.version = '7.17.0.1426';
        integrationModelProperties.scriptName = "wirisplugin-generic.js";
        integrationModelProperties.environment = {};
        integrationModelProperties.environment.editor = "GenericHTML";

        super(integrationModelProperties);

        this.toolbar = null;
        this.toolbar = integrationModelProperties.toolbar;
        if (typeof integrationModelProperties.configurationService !== 'undefined') {
            this.configurationService = integrationModelProperties.configurationService;
        }
    }

    /**
     * Returns the demo language, stored in _wrs_int_langCode variable. If the language
     * is no set set, calls parent getLanguage() method.
     * @returns {string} demo language.
     */
    getLanguage() {
        if (typeof _wrs_int_langCode !== 'undefined') {
            return  _wrs_int_langCode;
        } else {
            return super.getLanguage();
        }
    }

    /**@inheritdoc */
    callbackFunction() {
        // Call parent callbackFunction in order to addEvents to integration target.
        super.callbackFunction();
        /* Parsing input text */
        if (this.isIframe) {
            this.target.contentWindow.document.body.innerHTML = Parser.initParse(this.target.contentWindow.document.body.innerHTML);
        } else {
            this.target.innerHTML = Parser.initParse(this.target.innerHTML);
        }

        /* Creating toolbar buttons */
        var formulaButton = document.createElement('img');
        formulaButton.id = "editorIcon";
        formulaButton.src = formulaIcon;
        formulaButton.style.cursor = 'pointer';

        Util.addEvent(formulaButton, 'click', function () {
            this.core.getCustomEditors().disable();
            this.openNewFormulaEditor();
        }.bind(this));

        this.toolbar.appendChild(formulaButton);

        // Dynamic customEditors buttons.
        var customEditors = this.getCore().getCustomEditors();
        // Iterate from all custom editors.
        for (var customEditor in customEditors.editors) {
            if (customEditors.editors[customEditor].confVariable) {
                var customEditorButton = document.createElement('img');
                // TODO make this work and add promises polyfill
                // import('./icons/' + customEditors.editors[customEditor].icon).then(({default: customEditorIcon}) => {
                //     customEditorButton.src = customEditorIcon;
                // });
                // Horrible hard-coded temporary fix
                if (customEditor == 'chemistry') {
                    customEditorButton.src = chemIcon;
                }
                customEditorButton.id = customEditor + "Icon";
                customEditorButton.style.cursor = 'pointer';

                Util.addEvent(customEditorButton, 'click', function () {
                    customEditors.enable(customEditor);
                    this.openNewFormulaEditor();
                }.bind(this));

	                this.toolbar.appendChild(customEditorButton);
            }
        }
    }
}