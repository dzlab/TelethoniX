from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
import cgi


"""
print 'Content-Type: text/plain'
print ''
print 'Hello, world!'
"""

class Beats(webapp.RequestHandler):
    per_seconds = 3
    
    def get(self):
        self.response.headers['Content-Type'] = 'text/html'
        self.response.out.write(Beats.per_seconds)

    def post(self):
        beats = self.request.get('beats')
        Beats.per_seconds = beats
        self.response.out.write(Beats.per_seconds)
        
application = webapp.WSGIApplication([('/', Beats)], debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()