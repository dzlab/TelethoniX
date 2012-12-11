#!/usr/bin/python
'''This script read heart beats from Arduino through a serial connection, then send the data to a server'''

import serial
import httplib, urllib

ser = serial.Serial('/dev/ttyACM0',9600)

try:
	while 1:
		#read data from Arduino
		beats = ser.read(2)
		print 'beats: ' + str(beats)
		#send data to remote server
		server_addr = "post-webapp.appspot.com"
		params = urllib.urlencode({'beats': 12524})
		headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
		conn = httplib.HTTPConnection(server_addr)
		conn.request("POST", "/", params, headers)
		response = conn.getresponse()
		print response.status, response.reason
		data = response.read()
		print data
		conn.close()
except KeyboardInterrupt:
	print "done"