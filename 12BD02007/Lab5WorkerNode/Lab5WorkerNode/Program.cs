using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Messaging;
using System.Threading;
using TaskLib;

namespace Lab5WorkerNode
{
    class Program
    {
        static void Main(string[] args)
        {
            while (true) {
                if (!MessageQueue.Exists(MQueue.ConnectionTask))
                {
                    Console.WriteLine("Queue is not found");
                    Thread.Sleep(1000);
                    continue;
                }

                SubTask st = MQueue.ReceiveSubTask(MQueue.ConnectionTask);
                st.Run();
                Console.WriteLine(st.answer + " " + st.id + " " + st.lowerBound + " " + st.upperBound + " " + st.number);
            }
        }
    }
}
