/**
 *  Virtual Switch
 *
 *  Copyright 2016 Matt Francis 
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
        definition (name: "Virtual Switch", namespace: "mattfrancis", author: "Matt Francis") {
        capability "Switch"
        capability "Refresh"        
    }

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: false) {
			state "off", label: 'Off', action: "switch.on", icon: "st.Kids.kid10", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'On', action: "switch.off", icon: "st.Kids.kid10", backgroundColor: "#79b821", nextState: "off"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}        
		main "button"
		details(["button", "refresh"])
	}
}

def parse(String description) {
}

def on() {
	sendEvent(name: "switch", value: "on")
}

def off() {
	sendEvent(name: "switch", value: "off")
}

preferences {
    page(name: "selectActions")
}

def selectActions() {
    dynamicPage(name: "selectActions", title: "Select Hello Home Action to Execute", install: true, uninstall: true) {

        // get the available actions
            def actions = location.helloHome?.getPhrases()*.label
            if (actions) {
            // sort them alphabetically
            actions.sort()
                    section("Hello Home Actions") {
                            log.trace actions
                // use the actions as the options for an enum input
                input "action", "enum", title: "Select an action to execute", options: actions
                    }
            }
    }
}
