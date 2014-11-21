using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Messaging;
using System.Diagnostics;

namespace Lab5.Controllers
{
    public class HomeController : Controller
    {
        // GET: Home
        public ActionResult Index()
        {
            ViewBag.Title = "TWSTSA";
            ViewBag.action = "";
            ViewBag.number = 0;
            return View();
        }

        public ActionResult HandleForm(long number, string action)
        {
            ViewBag.number = number;
            Task t = new Task();
            t.number = number;
            t.todo = action;
            Random r = new Random();
            t.id = Stopwatch.GetTimestamp()+r.Next();
            Message m = new Message(t);
            m.Formatter = new XmlMessageFormatter(new Type[] { typeof(Task) });
            MessageQueue messageQueue = null;
            if (!MessageQueue.Exists(@".\Private$\ToRouter"))
                MessageQueue.Create(@".\Private$\ToRouter");
             using (messageQueue = new MessageQueue(@".\Private$\ToRouter"))
            {
                messageQueue.Label = "ToRouter";

                messageQueue.Send(m);
            }
            return View();
        }
    }
}