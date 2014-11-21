using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Messaging;
using System.Threading;
using System.Diagnostics;

namespace EulerWorker
{
    class Program
    {
        static void Main(string[] args)
        {
              Console.WriteLine("Worker is running");
              while (true)
              {
                  MessageQueue messageQueue = null;

                  using (messageQueue = new MessageQueue(@".\Private$\EulerQueue"))
                  {
                      System.Messaging.Message[] messages = messageQueue.GetAllMessages();

                      foreach (System.Messaging.Message message in messages)
                      {
                          messageQueue.Formatter = new XmlMessageFormatter(new Type[] { typeof(EulerClass) });
                          EulerClass fq = (EulerClass)messageQueue.Receive().Body;
                          Console.WriteLine("New task with id=" + fq.id.ToString());
                       
                          var sw = new Stopwatch();
                          sw.Start();

                          long local = fq.number;
                          long res = local;
                      //    long en = long.Parse((Math.Sqrt(double.Parse(local.ToString())) + 1).ToString());
                          long en = local;
                          for (long i = 2; i <= en; i++)
                              if ((local % i) == 0)
                              {
                                  while ((local % i) == 0)
                                      local /= i;
                                  res -= (res / i);
                              }
                          if (local > 1) res -= (res / local);
                            sw.Stop();
                            Console.WriteLine("Phi of " + fq.number.ToString() + " is " + res.ToString());
                            Console.WriteLine("Time elapsed: " + sw.ElapsedMilliseconds.ToString() + " ms");
                            EulerAnswerRAW fa = new EulerAnswerRAW();
                          fa.result = res;
                          fa.number = fq.number;
                          fa.elapsedMS = sw.ElapsedMilliseconds;

                          Message mA = new Message(fa);
                          MessageQueue eulerAnswers = null;
                          if (!MessageQueue.Exists(@".\Private$\EulerAnswers"))
                              MessageQueue.Create(@".\Private$\EulerAnswers");
                          using (eulerAnswers = new MessageQueue(@".\Private$\EulerAnswers"))
                          {
                              eulerAnswers.Label = "EulerAnswers";

                              eulerAnswers.Send(mA);
                          }
                      }
                      Thread.Sleep(2000);
                  }
              }
        }
    }
}
