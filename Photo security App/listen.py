import cmd
import locale
import os
import pprint
import shlex
import sys
import pkg_resources
from dropbox import session

from dropbox import client, rest

import json_parsing
import multiprocessing


# XXX Fill in your consumer key and secret below
# You can find these at http://www.dropbox.com/developers/apps
APP_KEY = 'iypm3vb6d6vxha0'
APP_SECRET = 'e1kq1rhpwu6dfhx'
ACCESS_TYPE = 'dropbox' # should be 'dropbox' or 'app_folder' as configured for your app


class DropboxTerm(cmd.Cmd):
    def __init__(self, app_key, app_secret):
        cmd.Cmd.__init__(self)
        self.sess = StoredSession(app_key, app_secret, access_type=ACCESS_TYPE)
        self.api_client = client.DropboxClient(self.sess)
        self.current_path = ''
        #self.prompt = "Dropbox> "
        self.sess.load_creds()

        while 1:		
       	    self.stdout.write("\nListening....")		
            f = self.api_client.get_file("desktopread")
            message=f.read()
	    if message<>" " and message<> "":
        	
                self.stdout.write("Change Found\nMessage is:")		
                self.stdout.write(message)

                multiprocessing.Process(target=self.run_process, args=(message,10)).start()
                
                self.stdout.write("\ndeleting contents of desktopread....")
                self.api_client.put_file("desktopread"," ",True)  
                     
	    else:						
        	self.stdout.write("No change found.")		
            f.close()
 	       

    def run_process(self,message,i):
     
        os.setsid()
        self.parse = json_parsing.Parsing()
        self.parse.parse_js_obj(message)



class StoredSession(session.DropboxSession):
    """a wrapper around DropboxSession that stores a token to a file on disk"""
    TOKEN_FILE = "token_store.txt"

    def load_creds(self):
        try:
            stored_creds = open(self.TOKEN_FILE).read()
            self.set_token(*stored_creds.split('|'))
            print "[loaded access token]"
        except IOError:
            pass # don't worry if it's not there

    def write_creds(self, token):
        f = open(self.TOKEN_FILE, 'w')
        f.write("|".join([token.key, token.secret]))
        f.close()

    def delete_creds(self):
        os.unlink(self.TOKEN_FILE)

    def link(self):
        request_token = self.obtain_request_token()
        url = self.build_authorize_url(request_token)
        print "url:", url
        print "Please authorize in the browser. After you're done, press enter."
        raw_input()

        self.obtain_access_token(request_token)
        self.write_creds(self.token)

    def unlink(self):
        self.delete_creds()
        session.DropboxSession.unlink(self)


def main():
    if APP_KEY == '' or APP_SECRET == '':
        exit("You need to set your APP_KEY and APP_SECRET!")
    term = DropboxTerm(APP_KEY, APP_SECRET)
    term.cmdloop()
    
  
if __name__ == '__main__':
    main()
