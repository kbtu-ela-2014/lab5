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

using System.Data.Entity;

namespace Lab5.Controllers
{
    public class EulerAnswersController : Controller
    {
        private EulerAnswerDBContext3 db = new EulerAnswerDBContext3();

        // GET: EulerAnswers
        public ActionResult Index()
        {
            this.checkQueue();
            return View(db.Answers.ToList());
        }

        private void checkQueue()
        {

            MessageQueue messageQueue = null;

            using (messageQueue = new MessageQueue(@".\Private$\EulerAnswers"))
            {
                System.Messaging.Message[] messages = messageQueue.GetAllMessages();

                foreach (System.Messaging.Message message in messages)
                {
                    messageQueue.Formatter = new XmlMessageFormatter(new Type[] { typeof(EulerAnswerRAW) });
                    EulerAnswerRAW fa = (EulerAnswerRAW)messageQueue.Receive().Body;
                    EulerAnswer to_be_inserted = new EulerAnswer
                    {
                        
                        elapsedMS = fa.elapsedMS,
                        task_id = fa.task_id,
                        number = fa.number,
                        result = fa.result
                    };
                    if (ModelState.IsValid)
                    {
                        // Thread.sus
                        db.Answers.Add(to_be_inserted);
                        db.SaveChanges();
                    }

                }
            }
        }
        // GET: EulerAnswers/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            EulerAnswer eulerAnswer = db.Answers.Find(id);
            if (eulerAnswer == null)
            {
                return HttpNotFound();
            }
            return View(eulerAnswer);
        }

        // GET: EulerAnswers/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: EulerAnswers/Create
        // Чтобы защититься от атак чрезмерной передачи данных, включите определенные свойства, для которых следует установить привязку. Дополнительные 
        // сведения см. в статье http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "id,number,task_id,result,elapsedMS")] EulerAnswer eulerAnswer)
        {
            if (ModelState.IsValid)
            {
                db.Answers.Add(eulerAnswer);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(eulerAnswer);
        }

        // GET: EulerAnswers/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            EulerAnswer eulerAnswer = db.Answers.Find(id);
            if (eulerAnswer == null)
            {
                return HttpNotFound();
            }
            return View(eulerAnswer);
        }

        // POST: EulerAnswers/Edit/5
        // Чтобы защититься от атак чрезмерной передачи данных, включите определенные свойства, для которых следует установить привязку. Дополнительные 
        // сведения см. в статье http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "id,number,task_id,result,elapsedMS")] EulerAnswer eulerAnswer)
        {
            if (ModelState.IsValid)
            {
                db.Entry(eulerAnswer).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(eulerAnswer);
        }

        // GET: EulerAnswers/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            EulerAnswer eulerAnswer = db.Answers.Find(id);
            if (eulerAnswer == null)
            {
                return HttpNotFound();
            }
            return View(eulerAnswer);
        }

        // POST: EulerAnswers/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            EulerAnswer eulerAnswer = db.Answers.Find(id);
            db.Answers.Remove(eulerAnswer);
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
