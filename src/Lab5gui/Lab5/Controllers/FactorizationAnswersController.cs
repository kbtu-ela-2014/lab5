using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using Lab5.Models;
using System.Messaging;
using System.Threading;

namespace Lab5.Controllers
{
    public class FactorizationAnswersController : Controller
    {
        private FactorizationAnswerDBContext3 db = new FactorizationAnswerDBContext3();

        // GET: FactorizationAnswers
        public ActionResult Index()
        {
           // Thread t = new Thread(() => this.checkQueue());
            //t.Start();
            this.checkQueue();
            return View(db.Answers.ToList());
        }
        private void checkQueue()
        {
           
                MessageQueue messageQueue = null;

                using (messageQueue = new MessageQueue(@".\Private$\FactorizationAnswers"))
                {
                    System.Messaging.Message[] messages = messageQueue.GetAllMessages();

                    foreach (System.Messaging.Message message in messages)
                    {
                        messageQueue.Formatter = new XmlMessageFormatter(new Type[] { typeof(FactorizationAnswerRAW) });
                        FactorizationAnswerRAW fa = (FactorizationAnswerRAW)messageQueue.Receive().Body;
                        FactorizationAnswer to_be_inserted = new FactorizationAnswer 
                        {
                        ans = fa.ans, 
                        answer = fa.answer,
                        elapsedMS = fa.elapsedMS,
                        task_id = fa.id,
                        number = fa.number
                        };
                        //  to_be_inserted.id = int.Parse(fa.id.ToString());
                        if (ModelState.IsValid)
                        {
                           // Thread.sus
                            db.Answers.Add(to_be_inserted);
                            db.SaveChanges();
                        }
                    
                }
            }
        }

        // GET: FactorizationAnswers/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            FactorizationAnswer factorizationAnswer = db.Answers.Find(id);
            if (factorizationAnswer == null)
            {
                return HttpNotFound();
            }
            return View(factorizationAnswer);
        }

        // GET: FactorizationAnswers/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: FactorizationAnswers/Create
        // Чтобы защититься от атак чрезмерной передачи данных, включите определенные свойства, для которых следует установить привязку. Дополнительные 
        // сведения см. в статье http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "id,typeOfAnswer,task_id,answer,elapsedMS")] FactorizationAnswer factorizationAnswer)
        {
            if (ModelState.IsValid)
            {
                db.Answers.Add(factorizationAnswer);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(factorizationAnswer);
        }

        // GET: FactorizationAnswers/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            FactorizationAnswer factorizationAnswer = db.Answers.Find(id);
            if (factorizationAnswer == null)
            {
                return HttpNotFound();
            }
            return View(factorizationAnswer);
        }

        // POST: FactorizationAnswers/Edit/5
        // Чтобы защититься от атак чрезмерной передачи данных, включите определенные свойства, для которых следует установить привязку. Дополнительные 
        // сведения см. в статье http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "id,typeOfAnswer,task_id,answer,elapsedMS")] FactorizationAnswer factorizationAnswer)
        {
            if (ModelState.IsValid)
            {
                db.Entry(factorizationAnswer).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(factorizationAnswer);
        }

        // GET: FactorizationAnswers/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            FactorizationAnswer factorizationAnswer = db.Answers.Find(id);
            if (factorizationAnswer == null)
            {
                return HttpNotFound();
            }
            return View(factorizationAnswer);
        }

        // POST: FactorizationAnswers/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            FactorizationAnswer factorizationAnswer = db.Answers.Find(id);
            db.Answers.Remove(factorizationAnswer);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
