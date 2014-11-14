using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Messaging;

namespace TaskLib
{
    public class TaskRouter: Task
    {
        public TaskRouter() : base() {}

        public static void run(SubTask t)
        {
            while (true)
            {
                Message m;
                using (MessageQueue msQ = new MessageQueue(MQueue.ConnectionTask))
                {
                    msQ.Formatter = new XmlMessageFormatter(new Type[] { typeof(SubTask) });
                    m = msQ.Receive();
                    SubTask st = (SubTask)m.Body;
                }

                using (MessageQueue msQ = new MessageQueue(MQueue.ConnectionAnswer))
                {
                    msQ.Send(m);
                }
            }
        }

        override protected void CreateSubTasks(int numberOfParall)
        {
            // Empty SubTask
            this.subtasks.Add(new SubTask());
        }

        override public void group()
        {
            // this method is never called
            return;
        }

    }
}
