using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Messaging;

namespace TaskLib
{
    public class MQueue
    {
        public static string ConnectionTask { get; private set; }
        public static string ConnectionAnswer { get; private set; }
        private static bool fromFile = false;

        static MQueue()
        {
            if (fromFile)
            {
                string text = System.IO.File.ReadAllText("..\\..\\Control\\Constant.txt") + "\n";

                int at = text.IndexOf("Task:"),
                    at2 = text.IndexOf("\n", at);
                ConnectionTask = text.Substring(at + 6, at2 - at - 6).Trim();

                at = text.IndexOf("Answer:");
                at2 = text.IndexOf("\n", at);
                ConnectionAnswer = text.Substring(at + 8, at2 - at - 8).Trim();
            }
            else 
            {
                ConnectionTask = @".\Private$\TaskQueue";
                ConnectionAnswer = @".\Private$\AnswerQueue";
            }
        }

        public static SubTask ReceiveSubTask(string connStr)
        {
            using (MessageQueue msQ = new MessageQueue(connStr))
            {
                msQ.Formatter = new XmlMessageFormatter(new Type[] { typeof(SubTask) });
                Message m = msQ.Receive();
                SubTask st = (SubTask)m.Body;
                return st;
            }
        }

        public static void SendSubTask(object obj, string connStr)
        {
            using (MessageQueue msQ = new MessageQueue(connStr))
            {
                Message m = new Message(obj);
                m.Label = ((SubTask)obj).id.ToString() + "_" + ((SubTask)obj).type;
                msQ.Send(m);
            }
        }
    }
}
