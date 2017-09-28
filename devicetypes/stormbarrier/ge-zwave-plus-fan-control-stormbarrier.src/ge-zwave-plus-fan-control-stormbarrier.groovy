/**
 *  GE Z-Wave Plus Fan Control
 * 
 *  A better functional Device Type for Z-Wave Smart Fan Control Switches
 *  Particularly the GE 14287 Z-Wave Smart Fan Control.
 * 
 *  Updated to work with the zwave plus version of the GE Fan Control and 
 *  heavily modified with Chris Nussbaum's GE Z-Wave Plus Switch DTH features.
 *
 *  Copyright 2015 ChadCK (Original Author)
 *  Copyright 2017 Chris Nussbaum (Features)
 *  Copyright 2017 stormbarrier (Recent Modifications)
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
	definition (name: "GE Z-Wave Plus Fan Control (stormbarrier)", namespace: "stormbarrier", author: "stormbarrier") {
		capability "Actuator"        
		capability "Button"
		capability "Configuration"        
		capability "Indicator"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
		capability "Switch"
		capability "Switch Level"

		attribute "currentState", "string"        
		attribute "inverted", "enum", ["inverted", "not inverted"]

		command "lowSpeed"
		command "medSpeed"
		command "highSpeed"
        command "doubleUp"
        command "doubleDown"
        command "inverted"
        command "notInverted"

        fingerprint mfr:"0063", prod:"4944", model:"3131", ver: "5.20", deviceJoinName: "GE Z-Wave Plus Fan Control"
	}

    preferences {
        input (
            type: "paragraph",
            element: "paragraph",
            title: "Configure Association Groups:",
            description: "Devices in association group 2 will receive Basic Set commands directly from the switch when it is turned on or off. Use this to control another device as if it was connected to this switch.\n\n" +
                         "Devices in association group 3 will receive Basic Set commands directly from the switch when it is double tapped up or down.\n\n" +
                         "Devices are entered as a comma delimited list of IDs in hexadecimal format."
        )

        input (
            name: "requestedGroup2",
            title: "Association Group 2 Members (Max of 5):",
            type: "text",
            required: false
        )

        input (
            name: "requestedGroup3",
            title: "Association Group 3 Members (Max of 4):",
            type: "text",
            required: false
        )
    }

	tiles (scale:2) {
		multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
			tileAttribute ("device.currentState", key: "PRIMARY_CONTROL") {
				attributeState "default", label:'ADJUSTING', action:"refresh.refresh", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"
				attributeState "HIGH", label:'HIGH', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#486e13", nextState: "turningOff"
				attributeState "MED", label:'MED', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#60931a", nextState: "turningOff"
				attributeState "LOW", label:'LOW', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#79b821", nextState: "turningOff"
				attributeState "OFF", label:'OFF', action:"switch.on", icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState: "turningOn"
				attributeState "turningOn", action:"switch.on", label:'TURNINGON', icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOn"
				attributeState "turningOff", action:"switch.off", label:'TURNINGOFF', icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"
			}
		}
		standardTile("lowSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2) {
        		state "default", label: 'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#ffffff"
			state "LOW", label:'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#79b821"
			state "ADJUSTING.LOW", label:'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"
  		}
		standardTile("medSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'MED', action: "medSpeed", icon:"st.Home.home30", backgroundColor: "#ffffff"
			state "MED", label: 'MED', action: "medSpeed", icon:"st.Home.home30", backgroundColor: "#79b821"
            		state "ADJUSTING.MED", label:'MED', action: "medSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"
		}
		standardTile("highSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'HIGH', action: "highSpeed", icon:"st.Home.home30", backgroundColor: "#ffffff"
			state "HIGH", label: 'HIGH', action: "highSpeed", icon:"st.Home.home30", backgroundColor: "#79b821"
            		state "ADJUSTING.HIGH", label:'HIGH', action: "highSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"
		}
        standardTile("doubleUp", "device.button", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Tap ▲▲", backgroundColor: "#ffffff", action: "doubleUp", icon: "https://raw.githubusercontent.com/nuttytree/Nutty-SmartThings/master/devicetypes/nuttytree/SwitchOnIcon.png"
		}      
        standardTile("doubleDown", "device.button", width: 2, height: 1, decoration: "flat") {
			state "default", label: "Tap ▼▼", backgroundColor: "#ffffff", action: "doubleDown", icon: "https://raw.githubusercontent.com/nuttytree/Nutty-SmartThings/master/devicetypes/nuttytree/SwitchOffIcon.png"
		} 
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
		standardTile("indicator", "device.indicatorStatus", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "when off", action:"indicator.indicatorWhenOn", icon:"st.indicators.lit-when-off"
			state "when on", action:"indicator.indicatorNever", icon:"st.indicators.lit-when-on"
			state "never", action:"indicator.indicatorWhenOff", icon:"st.indicators.never-lit"
		}

		main(["switch"])
		details(["switch", 
        		"lowSpeed", "medSpeed", "highSpeed", 
                "indicator", "doubleUp", "refresh", 
                "doubleDown"])
	}
	preferences {
		section("Fan Thresholds") {
			input "lowThreshold", "number", title: "Low Threshold", range: "1..99"
			input "medThreshold", "number", title: "Medium Threshold", range: "1..99"
			input "highThreshold", "number", title: "High Threshold", range: "1..99"
		}
	}
}

def getCommandClassVersions() {
	[
		0x85: 2,  // Association
        0x20: 1,  // Basic
		0x70: 2,  // Configuration
        0x56: 1,  // Crc16Encap
        0x26: 1,  // SwitchMultilevel
	]
}

def parse(String description) {
	log.debug "parse(): description is ${description}"
	def result = null
	def cmd = zwave.parse(description, commandClassVersions)
	if (cmd) {
		result = zwaveEvent(cmd)
        log.debug "Parsed ${cmd} to ${result.inspect()}"
	}
	else {
        log.debug "Non-parsed event: ${description}"
	}
	result
}

def zwaveEvent(physicalgraph.zwave.commands.crc16encapv1.Crc16Encap cmd) {
	log.debug "zwaveEvent(): CRC-16 Un-encapsulating command: ${cmd}"
    def versions = commandClassVersions
	def version = versions[cmd.commandClass as Integer]
	def ccObj = version ? zwave.commandClass(cmd.commandClass, version) : zwave.commandClass(cmd.commandClass)
	def encapsulatedCommand = ccObj?.command(cmd.command)?.parse(cmd.data)
	if (encapsulatedCommand) {
		return zwaveEvent(encapsulatedCommand)
	}
    else {
    	log.debug("zwaveEvent(): CRC-16 Could not un-encapsulate command from ${cmd}")
    }
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
	dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
	if (cmd.value == 255) {
        doubleUp(true)
    }
	else if (cmd.value == 0) {
        doubleDown(true)
    }
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelStartLevelChange cmd) {
	[]
	log.trace "StartLevel"
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelStopLevelChange cmd) {
	[response(zwave.basicV1.basicGet())]
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelSet cmd) {
	dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
	dimmerEvents(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationReport cmd) {
	log.debug "---ASSOCIATION REPORT V2--- ${device.displayName} sent groupingIdentifier: ${cmd.groupingIdentifier} maxNodesSupported: ${cmd.maxNodesSupported} nodeId: ${cmd.nodeId} reportsToFollow: ${cmd.reportsToFollow}"
    state.group3 = "1,2"
    if (cmd.groupingIdentifier == 3) {
    	if (cmd.nodeId.contains(zwaveHubNodeId)) {
        	createEvent(name: "numberOfButtons", value: 2, displayed: false)
        }
        else {
        	sendEvent(name: "numberOfButtons", value: 0, displayed: false)
			sendHubCommand(new physicalgraph.device.HubAction(zwave.associationV2.associationSet(groupingIdentifier: 3, nodeId: zwaveHubNodeId).format()))
			sendHubCommand(new physicalgraph.device.HubAction(zwave.associationV2.associationGet(groupingIdentifier: 3).format()))
        }
    }
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd) {
	log.debug "---CONFIGURATION REPORT V2--- ${device.displayName} sent ${cmd}"
	def name = ""
    def value = ""
    def reportValue = cmd.scaledConfigurationValue
    switch (cmd.parameterNumber) {
        case 3:
            name = "indicatorStatus"
            value = reportValue == 1 ? "when on" : reportValue == 2 ? "never" : "when off"
            break
        case 4:
            name = "inverted"
            value = reportValue == 1 ? "true" : "false"
            break
        default:
        	break
	}
	createEvent([name: name, value: value, displayed: false])
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
	log.debug "manufacturerId:   ${cmd.manufacturerId}"
	log.debug "manufacturerName: ${cmd.manufacturerName}"
	log.debug "productId:        ${cmd.productId}"
	log.debug "productTypeId:    ${cmd.productTypeId}"
	def msr = String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId)
	updateDataValue("MSR", msr)
	updateDataValue("manufacturer", cmd.manufacturerName)
	createEvent([descriptionText: "$device.displayName MSR: $msr", isStateChange: false])
}

def dimmerEvents(physicalgraph.zwave.Command cmd) {
	log.debug "dimmerEvents(): cmd is ${cmd}"

	def value = (cmd.value ? "on" : "off")
	if (value == "off") { // If the fan was turned off, update currentState
		sendEvent(name: "currentState", value: "OFF" as String)
	}
	def result = [createEvent(name: "switch", value: value)]
    
    // Assign thresholds to compare against for currentState
	def lowThresholdvalue = (settings.lowThreshold != null && settings.lowThreshold != "") ? settings.lowThreshold.toInteger() : 33
	def medThresholdvalue = (settings.medThreshold != null && settings.medThreshold != "") ? settings.medThreshold.toInteger() : 67
	def highThresholdvalue = (settings.highThreshold != null && settings.highThreshold != "") ? settings.highThreshold.toInteger() : 100
    
    // If the dimming value changed, update our currentState
	if (cmd.value && cmd.value <= 100) {
		if (cmd.value <= lowThresholdvalue) { sendEvent(name: "currentState", value: "LOW" as String) }
		if (cmd.value >= lowThresholdvalue+1 && cmd.value <= medThresholdvalue) { sendEvent(name: "currentState", value: "MED" as String) }
		if (cmd.value >= medThresholdvalue+1) { sendEvent(name: "currentState", value: "HIGH" as String) }
		result << createEvent(name: "level", value: cmd.value, unit: "%")
	}
	result
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
	// Handles any Z-Wave commands we aren't interested in
	log.warn "${device.displayName} received unhandled command: ${cmd}"
}

def configure() {
    // Get current config parameter values
    def cmds = []
    
    // LED Indicator
    // 0 = lit while Off (default)
    // 1 = lit while On
    // 2 = never lit
    cmds << zwave.configurationV2.configurationGet(parameterNumber: 3).format()
    
    // Inverted Switch
    // 0 = default
    // 1 = inverted
    cmds << zwave.configurationV2.configurationGet(parameterNumber: 4).format()
    
    // Add the hub to association group 3 to get double-tap notifications
    cmds << zwave.associationV2.associationSet(groupingIdentifier: 3, nodeId: zwaveHubNodeId).format()
    cmds << zwave.associationV2.associationGet(groupingIdentifier: 3).format()
    
    delayBetween(cmds,500)
}

def updated() {
    if (state.lastUpdated && now() <= state.lastUpdated + 3000) return
    state.lastUpdated = now()

	def nodes = []
    def cmds = []

	if (settings.requestedGroup2 != state.currentGroup2) {
        nodes = parseAssocGroupList(settings.requestedGroup2, 2)
        cmds << zwave.associationV2.associationRemove(groupingIdentifier: 2, nodeId: [])
        cmds << zwave.associationV2.associationSet(groupingIdentifier: 2, nodeId: nodes)
        cmds << zwave.associationV2.associationGet(groupingIdentifier: 2)
        state.currentGroup2 = settings.requestedGroup2
    }

    if (settings.requestedGroup3 != state.currentGroup3) {
        nodes = parseAssocGroupList(settings.requestedGroup3, 3)
        cmds << zwave.associationV2.associationRemove(groupingIdentifier: 3, nodeId: [])
        cmds << zwave.associationV2.associationSet(groupingIdentifier: 3, nodeId: nodes)
        cmds << zwave.associationV2.associationGet(groupingIdentifier: 3)
        state.currentGroup3 = settings.requestedGroup3
    }

	sendHubCommand(cmds.collect{ new physicalgraph.device.HubAction(it.format()) }, 500)
}

def on() {
	delayBetween([zwave.basicV1.basicSet(value: 0xFF).format(), zwave.switchMultilevelV1.switchMultilevelGet().format()], 1000)
}

def off() {
	delayBetween ([zwave.basicV1.basicSet(value: 0x00).format(), zwave.switchMultilevelV1.switchMultilevelGet().format()], 1000)
}

def setLevel(value) {
	def lowThresholdvalue = (settings.lowThreshold != null && settings.lowThreshold != "") ? settings.lowThreshold.toInteger() : 33
	def medThresholdvalue = (settings.medThreshold != null && settings.medThreshold != "") ? settings.medThreshold.toInteger() : 67
	def highThresholdvalue = (settings.highThreshold != null && settings.highThreshold != "") ? settings.highThreshold.toInteger() : 99
	
	if (value == "LOW") { value = lowThresholdvalue }
	if (value == "MED") { value = medThresholdvalue }
	if (value == "HIGH") { value = highThresholdvalue }

	def level = Math.min(value as Integer, 99)
	
	log.trace "setLevel(value): ${level}"
    
	if (level <= lowThresholdvalue) { sendEvent(name: "currentState", value: "ADJUSTING.LOW" as String, displayed: false) }
	if (level >= lowThresholdvalue+1 && level <= medThresholdvalue) { sendEvent(name: "currentState", value: "ADJUSTING.MED" as String, displayed: false) }
	if (level >= medThresholdvalue+1) { sendEvent(name: "currentState", value: "ADJUSTING.HIGH" as String, displayed: false) }
	
	delayBetween ([zwave.basicV1.basicSet(value: level as Integer).format(), zwave.switchMultilevelV1.switchMultilevelGet().format()], 500)
}

def setLevel(value, duration) {
	def lowThresholdvalue = (settings.lowThreshold != null && settings.lowThreshold != "") ? settings.lowThreshold.toInteger() : 33
	def medThresholdvalue = (settings.medThreshold != null && settings.medThreshold != "") ? settings.medThreshold.toInteger() : 67
	def highThresholdvalue = (settings.highThreshold != null && settings.highThreshold != "") ? settings.highThreshold.toInteger() : 99

	if (value == "LOW") { value = lowThresholdvalue }
	if (value == "MED") { value = medThresholdvalue }
	if (value == "HIGH") { value = highThresholdvalue }

	def level = Math.min(value as Integer, 99)
	def dimmingDuration = duration < 128 ? duration : 128 + Math.round(duration / 60)
	
	log.trace "setLevel(value): ${level}"
	
	if (level <= lowThresholdvalue) { sendEvent(name: "currentState", value: "ADJUSTING.LOW" as String, displayed: false) }
	if (level >= lowThresholdvalue+1 && level <= medThresholdvalue) { sendEvent(name: "currentState", value: "ADJUSTING.MED" as String, displayed: false) }
	if (level >= medThresholdvalue+1) { sendEvent(name: "currentState", value: "ADJUSTING.HIGH" as String, displayed: false) }
    
	zwave.switchMultilevelV2.switchMultilevelSet(value: level, dimmingDuration: dimmingDuration).format()
}

def lowSpeed() {
	setLevel("LOW")
}

def medSpeed() {
	setLevel("MED")
}

def highSpeed() {
	setLevel("HIGH")
}

def poll() {
	zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def refresh() {
	log.debug "refresh() is called"
	def cmds = []
	cmds << zwave.switchMultilevelV1.switchMultilevelGet().format()
    cmds << zwave.configurationV2.configurationGet(parameterNumber: 3).format()
    cmds << zwave.configurationV2.configurationGet(parameterNumber: 4).format()
    cmds << zwave.associationV2.associationGet(groupingIdentifier: 3).format()
	if (getDataValue("MSR") == null) {
		cmds << zwave.manufacturerSpecificV1.manufacturerSpecificGet().format()
	}
	delayBetween(cmds,500)
}

def indicatorWhenOn() {
	sendEvent(name: "indicatorStatus", value: "when on", display: false)
	zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 3, size: 1).format()
}

def indicatorWhenOff() {
	sendEvent(name: "indicatorStatus", value: "when off", display: false)
	zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 3, size: 1).format()
}

def indicatorNever() {
	sendEvent(name: "indicatorStatus", value: "never", display: false)
	zwave.configurationV1.configurationSet(configurationValue: [2], parameterNumber: 3, size: 1).format()
}

def inverted() {
	sendEvent(name: "inverted", value: "inverted", display: false)
	zwave.configurationV2.configurationSet(configurationValue: [1], parameterNumber: 4, size: 1).format()
}

def notInverted() {
	sendEvent(name: "inverted", value: "not inverted", display: false)
	zwave.configurationV2.configurationSet(configurationValue: [0], parameterNumber: 4, size: 1).format()
}

def doubleUp() {
	doubleUp(false)
}

def doubleUp(isPhysical) {
	def pressType = isPhysical == true ? "physical" : "digital"
	sendEvent(name: "button", value: "pushed", data: [buttonNumber: 1], descriptionText: "Double-tap up (button 1) on $device.displayName", isStateChange: true, type: pressType)
}

def doubleDown() {
	doubleDown(false)
}

def doubleDown(isPhysical) {
	def pressType = isPhysical == true ? "physical" : "digital"
	sendEvent(name: "button", value: "pushed", data: [buttonNumber: 2], descriptionText: "Double-tap down (button 2) on $device.displayName", isStateChange: true, type: pressType)
}

// Private Methods

private parseAssocGroupList(list, group) {
    def nodes = group == 2 ? [] : [zwaveHubNodeId]
    if (list) {
        def nodeList = list.split(',')
        def max = group == 2 ? 5 : 4
        def count = 0

        nodeList.each { node ->
            node = node.trim()
            if ( count >= max) {
                log.warn "Association Group ${group}: Number of members is greater than ${max}! The following member was discarded: ${node}"
            }
            else if (node.matches("\\p{XDigit}+")) {
                def nodeId = Integer.parseInt(node,16)
                if (nodeId == zwaveHubNodeId) {
                	log.warn "Association Group ${group}: Adding the hub as an association is not allowed (it would break double-tap)."
                }
                else if ( (nodeId > 0) & (nodeId < 256) ) {
                    nodes << nodeId
                    count++
                }
                else {
                    log.warn "Association Group ${group}: Invalid member: ${node}"
                }
            }
            else {
                log.warn "Association Group ${group}: Invalid member: ${node}"
            }
        }
    }
    
    return nodes
}