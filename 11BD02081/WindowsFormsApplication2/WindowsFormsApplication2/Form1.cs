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

namespace WindowsFormsApplication2
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
            string QueueName = ".\\Private$\\prime";
            MessageQueue Q1 = new MessageQueue(QueueName);
            try
            {
                System.Messaging.Message ms = Q1.Receive();
                ms.Formatter = new XmlMessageFormatter(new Type[] { typeof(string) });
                int a = Convert.ToInt32(ms.Body.ToString());
                int sum = 0;
                bool prime = true;
                for (int i = 2; i <= Math.Sqrt(a); i++) if(a%i==0) prime=false;
                string Queue2 = ".\\Private$\\prime_ans";
                if (!MessageQueue.Exists(Queue2)) MessageQueue.Create(Queue2);
                MessageQueue Q2 = new MessageQueue(Queue2);
                string ans;
                if (prime) ans = "YES, "+Convert.ToString(a)+" is prime number";
                else ans = "NO, " + Convert.ToString(a) + " is not prime number";
                Q2.Send(ans);
                Q2.Close();
                solve();
            }
            catch (Exception ex)
            {
            }
        }
    }
}
