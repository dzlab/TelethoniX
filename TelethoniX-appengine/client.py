import httplib, urllib


#Do get
#server_addr = "localhost:8080"
server_addr = "post-webapp.appspot.com"
conn = httplib.HTTPConnection(server_addr)
conn.request("GET", "/")
rsp = conn.getresponse()
print rsp.status, rsp.reason

data = rsp.read()
conn.close()
print data


#Do post
params = urllib.urlencode({'beats': 12524})
headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
conn = httplib.HTTPConnection(server_addr)
conn.request("POST", "/", params, headers)
response = conn.getresponse()
print response.status, response.reason

data = response.read()
print data
data = 'Redirecting to <a href="http://bugs.python.org/issue12524">http://bugs.python.org/issue12524</a>'
conn.close()
