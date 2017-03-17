/**
 *  Switch to Run Routine 
 *
 *  Copyright 2017 Matthew Francis
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
definition(
    name: "Switch to Run Routine ",
    namespace: "mlfrancis123",
    author: "Matthew Francis",
    description: "Used to run a routine for a switch ",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Settings") {
		// TODO: put inputs here
        input "controlSwitch", "capability.switch"
        
        // select routine 
        input "routine", "text", required: true, title: "Routine Name?"
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
    
    // init 
    initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    subscribe(controlSwitch, "switch", "ControlswitchHandler")
}

// TODO: implement event handlers
def ControlswitchHandler(evt) {
	if (evt.value == "on") {
    	log.debug "*****Switch On******"
        
        // run the routine 
        log.debug "*****Run Routine****** (${settings.routine})"
        location.helloHome?.execute(settings.routine)
        
        // turn the switch off 
        log.debug "*****Switch Off******"
        controlSwitch.off()
        
    } else {
    	log.debug "Switch Off"
    }
}