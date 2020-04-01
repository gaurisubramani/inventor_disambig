/* Copyright (C) 2016 University of Massachusetts Amherst.
   This file is part of “inventor_disambiguation”

   This work was done for the USPTO inventor disambiguation workshop
   organized under the PatentsView initiative (www.patentsview.org).
   The algorithm was the best performing at the workshop according
   to the workshop judges' criteria of disambiguation performance,
   running time, and usability.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */


package edu.umass.cs.iesl.inventor_disambiguation.data_structures.coreference

import edu.umass.cs.iesl.inventor_disambiguation.coreference.ReEvaluatingNameProcessor
import edu.umass.cs.iesl.inventor_disambiguation.data_structures._
import edu.umass.cs.iesl.inventor_disambiguation.data_structures.classification.{USPC}
import edu.umass.cs.iesl.inventor_disambiguation.db.Datastore
import edu.umass.cs.iesl.inventor_disambiguation._

/**
 * Inventor mentions are the main input to the disambiguation algorithm. These objects
 * are stored in a mongo database and retrieved by their inventor's id.  
 * The mentions contain the different pieces of data pertaining to a particular inventor.
 * Like the other data structures in this project, the inventor mention is descent of the 
 * data type Cubbie (from factorie) which provides easy serialization to Mongo. 
 */
class InventorMention extends ApplicationViewRecord{

  val mentionID = new StringSlot("mention_id")

  val self = new CubbieSlot[Inventor]("self",() => new Inventor())

  // The inventors original name
  val rawName = new CubbieSlot[PersonNameRecord]("rawName", () => new PersonNameRecord())

  val patent = new CubbieSlot[Patent]("patent", () => new Patent())
  val coInventors = new CubbieListSlot[Inventor]("coInventors", () => new Inventor())

  lazy val coInventorLastnames = coInventors.value.flatMap(_.nameLast.opt)
  lazy val coInventorLastnamesBag = coInventorLastnames.counts

  val entityId = new StringSlot("entityId")

  // Other data about the patent itself
  val assignees = new CubbieListSlot[Assignee]("assignees", () => new Assignee())

  val lawyers = new CubbieListSlot[Lawyer]("lawyers", () => new Lawyer())

  val uspc = new CubbieListSlot[USPC]("uspc", () => new USPC())

  val location = new CubbieSlot[Location]("location", () => new Location())

//  def this(self: Inventor, patent: Patent, coInventors: Seq[Inventor]) = {
//    this()
//    this.mentionID.set(self.inventorID.value)
//    this.self.set(self)
//    this.applicationNumber.set(self.applicationNumber.opt)
//    this.patent.set(patent)
//    this.coInventors.set(coInventors)
//  }
}

/**
 * Mentions to generate an inventor mention from Datastores.
 */
object InventorMention {

  def fromDatastores(self: Inventor,
                     assigneeDB: Datastore[String,Assignee],
                     inventorDB: Datastore[String,Inventor],
                     lawyerDB: Datastore[String,Lawyer],
                     locationDB: Datastore[String, Location],
                     patentDB: Datastore[String,Patent],
                     uspcDB: Datastore[String,USPC]): InventorMention = {

    val applicationNumber = self.applicationNumber.value
    val maybePatent = patentDB.get(applicationNumber)
    if (maybePatent.isEmpty) println(s"[${this.getClass.getSimpleName}] WARNING:  We must have a patent for the inventor with application: ${self.applicationNumber.value}, ${self.nameFirst.value}, ${self.nameLast.value} with patent ${self.applicationNumber.value}")
    val mention = new InventorMention()
    mention.self.set(self)

    // Take the raw name split it up into middle and suffixes
    mention.rawName.set(new PersonNameRecord(self.nameFirst.opt,self.nameMiddles.opt,self.nameLast.opt,self.nameSuffixes.opt))
    ReEvaluatingNameProcessor.process(mention.rawName.value)

    mention.mentionID.set(self.inventorID.value)
    mention.patent.set(maybePatent.headOption)

    val coInventors = inventorDB.get(applicationNumber).filter(_.inventorID.value != self.inventorID.value)
    mention.coInventors.set(coInventors.toSeq)

    mention.assignees.set(assigneeDB.get(self.applicationNumber.value).toList)
    mention.lawyers.set(lawyerDB.get(self.applicationNumber.value).toList)
    mention.uspc.set(uspcDB.get(self.applicationNumber.value).toList)
    mention
  }
}




