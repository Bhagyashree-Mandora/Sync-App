import pickle
import gtk
import photoapp


# A default initialisation of object before pickling.    
message = {"sender": "my laptop","receiver": "my mobile", "command": "takephoto","req_id":"123","data":{"content": "Hi...string!!", "date": "27", "sender": "ME", "time": "4:30"}}

trial = photoapp.ConcreteClass(message)

output = open('takephoto.pkl', 'wb')

# Pickle dictionary using protocol 0.
pickle.dump(trial, output)

# Pickle the list using the highest protocol available.
# pickle.dump(selfref_list, output, -1)

print 'done 2...'

output.close()
        
