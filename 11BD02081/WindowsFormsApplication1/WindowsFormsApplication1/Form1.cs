using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Messaging;

namespace WindowsFormsApplication1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            solve();
        }
        void solve()
        {
            string QueueName = ".\\Private$\\sum";
            MessageQueue Q1 = new MessageQueue(QueueName);
            try
            {
                System.Messaging.Message ms = Q1.Receive();
                ms.Formatter = new XmlMessageFormatter(new Type[] { typeof(string) });
                int a = Convert.ToInt32(ms.Body.ToString());
                int sum = 0;
                for (int i = 1; i <= a; i++) sum += i;
                string Queue2 = ".\\Private$\\sum_ans";
                if (!MessageQueue.Exists(Queue2)) MessageQueue.Create(Queue2);
                MessageQueue Q2 = new MessageQueue(Queue2);
                Q2.Send(sum);
                solve();
            }
            catch (Exception ex)
            {
            }
        }
    }
}
