using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;
using System.Messaging;
using System.Threading;

namespace Worker
{
    class Program
    {
        

        static void Main(string[] args)
        {
            Console.WriteLine("Worker is running");
            while (true)
            {
                MessageQueue messageQueue = null;

                using (messageQueue = new MessageQueue(@".\Private$\FactorizationQueue"))
                {
                    System.Messaging.Message[] messages = messageQueue.GetAllMessages();

                    foreach (System.Messaging.Message message in messages)
                    {
                        messageQueue.Formatter = new XmlMessageFormatter(new Type[] { typeof(FactorizationClass) });
                        FactorizationClass fq = (FactorizationClass)messageQueue.Receive().Body;
                        Console.WriteLine("New task with id="+fq.id.ToString());
                        var subject = fq.number;
                        var factors = new List<long>();
                        var maxFactor = 0;

                        Console.WriteLine("Factoring {0} ...", subject);

                        var sw = new Stopwatch();
                        sw.Start();

                        while (subject > 1)
                        {
                            var nextFactor = 2;
                            if (subject % nextFactor > 0)
                            {
                                nextFactor = 3;
                                do
                                {
                                    if (subject % nextFactor == 0)
                                    {
                                        break;
                                    }

                                    nextFactor += 2;
                                } while (nextFactor < subject);
                            }

                            subject /= nextFactor;
                            factors.Add(nextFactor);
                            if (nextFactor > maxFactor)
                            {
                                maxFactor = nextFactor;
                            }
                        }

                        sw.Stop();

                        var factorAnswer = 1L;
                        factors.ForEach(f => factorAnswer *= f);

                        Console.WriteLine("Factors: {0} = {1}",
                            string.Join(" * ",
                                factors.Select(i => i.ToString()).ToArray()),
                            factorAnswer);
                        Console.WriteLine("Elapsed Time: {0}ms", sw.ElapsedMilliseconds);
                        FactorizationAnswerRAW fa = new FactorizationAnswerRAW();
                        fa.id = fq.id;
                        fa.typeOfAnswer = "factorization";
                        fa.ans = factors;
                        fa.number = fq.number;
                        factors.ForEach(f => factorAnswer *= f);

                           fa.answer = string.Join(" * ",
                                factors.Select(i => i.ToString()).ToArray());
                        
                        fa.elapsedMS = sw.ElapsedMilliseconds;
                       
                        Message mA = new Message(fa);
                        MessageQueue factorizationAnswers = null;
                        if (!MessageQueue.Exists(@".\Private$\FactorizationAnswers"))
                            MessageQueue.Create(@".\Private$\FactorizationAnswers");
                        using (factorizationAnswers = new MessageQueue(@".\Private$\FactorizationAnswers"))
                        {
                            factorizationAnswers.Label = "FactorizationAnswers";

                            factorizationAnswers.Send(mA);
                        }
                    }
                    Thread.Sleep(2000);
                }
            }

        }
        
    


    }
}
