using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Messaging;
using System.Threading;
using TaskLib;

namespace Lab5MainNode
{
    class Program
    {
        static void Main(string[] args)
        {
            createQueues();

            int id = TaskManager.NewTask(Tasks.TESTPRIME,2500000, 2);
            while (TaskManager.GetAnswerById(id) == null) 
                Thread.Sleep(10);

            string ans = TaskManager.GetAnswerById(id).ToString();
            

            Console.WriteLine(ans);

            //TaskManager.Dispose();
        }

        public static void createQueues()
        {
            if (!MessageQueue.Exists(MQueue.ConnectionTask))
                MessageQueue.Create(MQueue.ConnectionTask);
            if (!MessageQueue.Exists(MQueue.ConnectionAnswer))
                MessageQueue.Create(MQueue.ConnectionAnswer);
        }
    }
}
