/**
 *  My First Smart App
 *
 *  Copyright 2016 Matthew Francis
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
    name: "Light on with Motion",
    namespace: "mattfrancis",
    author: "Matthew Francis",
    description: "Desc TBD",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Turn on when motion detected:") {
        input "themotion", "capability.motionSensor", required: true, title: "Where?"
    }
    section("Turn on this light") {
        input "theswitch", "capability.switch", required: true
    }
    section("Turn off when there's been no movement for") {
        input "minutes", "number", required: true, title: "Minutes?"
    }
    section("Name this automation") {
        input "name", "string", required: true, title: "Name?"
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    
    // motion active event
    subscribe(themotion, "motion.active", motionDetectedHandler)
    
    // motion not active
    subscribe(themotion, "motion.inactive", motionStoppedHandler)
    
    // humidity
    subscribe(themotion, "humidity", humidityHandler)
}

// TODO: implement event handlers

// log when humidity changes
def humidityHandler(evt) {
    log.debug "humidity changed: $evt"
    
    //sendNotification("Motion found, lights turning on: " + name)
    // turn on light
    //theswitch.on()
}

// turn on lights when motion detected
def motionDetectedHandler(evt) {
    log.debug "motionDetectedHandler called: $evt"
    
    //sendNotification("Motion found, lights turning on: " + name)
    // turn on light
    theswitch.on()
}

// turn off lights after motion stops
def motionStoppedHandler(evt) {
    log.debug "motionStoppedHandler called: $evt"
    runIn(60 * minutes, checkMotion)
}

def checkMotion() {
    log.debug "In checkMotion scheduled method"

    // get the current state object for the motion sensor
    def motionState = themotion.currentState("motion")

    if (motionState.value == "inactive") {
        // get the time elapsed between now and when the motion reported inactive
        def elapsed = now() - motionState.date.time

        // elapsed time is in milliseconds, so the threshold must be converted to milliseconds too
        def threshold = 1000 * 60 * minutes

            if (elapsed >= threshold) {
            log.debug "Motion has stayed inactive long enough since last check ($elapsed ms):  turning switch off"
            theswitch.off()
            } else {
            log.debug "Motion has not stayed inactive long enough since last check ($elapsed ms):  doing nothing"
        }
    } else {
            // Motion active; just log it and do nothing
            log.debug "Motion is active, do nothing and wait for inactive"
    }
}