import pickle
import gtk
import terminalapp


# A default initialisation of object before pickling.    
message = {"sender": "my laptop","receiver": "my mobile", "command": "takephoto","requestId":"123","data":{"content": "Hi...string!!","requestId": "12", "date": "27", "sender": "ME", "time": "4:30"}}

trial = terminalapp.ConcreteClass(message)

output = open('terminalapp.pkl', 'wb')

# Pickle dictionary using protocol 0.
pickle.dump(trial, output)

# Pickle the list using the highest protocol available.
# pickle.dump(selfref_list, output, -1)

print 'done 2...'

output.close()
        
