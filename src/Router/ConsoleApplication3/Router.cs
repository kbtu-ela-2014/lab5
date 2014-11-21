using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Messaging;
namespace ConsoleApplication3
{
    class Router
    {

        static void Main(string[] args)
        {

            Console.WriteLine("Router is running");
            while (true) {
            MessageQueue messageQueue = null;
            
                using (messageQueue = new MessageQueue(@".\Private$\ToRouter"))
                {
                       System.Messaging.Message[] messages = messageQueue.GetAllMessages();

                     foreach (System.Messaging.Message message in messages)
                    {
                        messageQueue.Formatter = new XmlMessageFormatter(new Type[] { typeof(Task) });
                        Task t = (Task)messageQueue.Receive().Body;
                         if(t.todo.Equals("factorization"))
                         {
                             FactorizationClass fq = new FactorizationClass();
                             fq.id = t.id;
                             fq.number = t.number;
                             Message mF = new Message(fq);
                             MessageQueue messageQueueFactorization = null;
                                if (!MessageQueue.Exists(@".\Private$\FactorizationQueue"))
                                     MessageQueue.Create(@".\Private$\FactorizationQueue");
                                 using (messageQueueFactorization = new MessageQueue(@".\Private$\FactorizationQueue"))
                                 {
                                     messageQueueFactorization.Label = "FactorizationQueue";

                                     messageQueueFactorization.Send(mF);
                                 }
                         }
                         if (t.todo.Equals("euler"))
                         {
                             EulerClass eq = new EulerClass();
                             eq.id = t.id;
                             eq.number = t.number;
                             Message mF = new Message(eq);
                             MessageQueue messageQueueEuler = null;
                             if (!MessageQueue.Exists(@".\Private$\EulerQueue"))
                                 MessageQueue.Create(@".\Private$\EulerQueue");
                             using (messageQueueEuler = new MessageQueue(@".\Private$\EulerQueue"))
                             {
                                
                                 messageQueueEuler.Label = "EulerQueue";

                                 messageQueueEuler.Send(mF);
                             }
                         }
                        Console.WriteLine("New: id:"+t.id.ToString()+"Number: "+t.number.ToString()+"Task: "+t.todo);

                    }
                     Thread.Sleep(2000);
                }
            }
        }
    }
}