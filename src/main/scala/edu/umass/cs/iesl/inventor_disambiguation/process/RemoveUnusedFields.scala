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


package edu.umass.cs.iesl.inventor_disambiguation.process

import java.io.{PrintWriter, File}

import edu.umass.cs.iesl.inventor_disambiguation.data_structures.classification.USPC
import edu.umass.cs.iesl.inventor_disambiguation.data_structures._
import edu.umass.cs.iesl.inventor_disambiguation.data_structures.coreference.InventorMention
import edu.umass.cs.iesl.inventor_disambiguation.load.LoadJSONInventorMentions
import edu.umass.cs.iesl.inventor_disambiguation.utilities.PatentJsonSerialization

/**
  * Remove all fields from inventor mentions
  * that are not used in the algorithm.
  */
object RemoveUnusedFields {

  def reduce(im: InventorMention) = {
    val reduced = new InventorMention()
    reduced.mentionID.set(im.mentionID.value)
    reduced.applicationNumber.set(im.applicationNumber.value)
    reduced.assignees.set(im.assignees.opt.getOrElse(Seq()).map(a => reduceAssignee(a)))
    reduced.coInventors.set(im.coInventors.opt.getOrElse(Seq()).map(c => reduceCoInventor(c)))
    reduced.lawyers.set(im.lawyers.opt.getOrElse(Seq()).map(i => reduceLawyer(i)))
    reduced.patent.set(reducePatent(im.patent.value))
    reduced.self.set(reduceSelf(im.self.value))
    reduced.uspc.set(im.uspc.opt.getOrElse(Seq()).map(n => reduceUSPC(n)))
    reduced
  }

  def reduceAssignee(a: Assignee) = {
    val reduced = new Assignee()
    reduced.uuid.set(a.uuid.opt)
    reduced.organization.set(a.organization.opt)
    reduced
  }

  def reduceCoInventor(inv: Inventor) = {
    val reduced = new Inventor()
    reduced.nameFirst.set(inv.nameFirst.opt)
    reduced.nameLast.set(inv.nameLast.opt)
    reduced
  }

  def reduceLawyer(lawyer: Lawyer) = {
    val reduced = new Lawyer()

    reduced.uuid.set(lawyer.uuid.opt)
    reduced.country.set(lawyer.country.opt)
    reduced.nameLine1.set(lawyer.nameLine1.opt)
    reduced.nameLine2.set(lawyer.nameLine2.opt)
    reduced.streetLine1.set(lawyer.streetLine1.opt)
    reduced.streetLine2.set(lawyer.streetLine2.opt)
    reduced.city.set(lawyer.city .opt)
    reduced.postalCode.set(lawyer.postalCode.opt)
    reduced.regionCode.set(lawyer.regionCode.opt)
    reduced.countryCode.set(lawyer.countryCode.opt)
    reduced.countryName.set(lawyer.countryName.opt)
    reduced
  }

  def reducePatent(patent: Patent) = {
    val reduced = new Patent()
    reduced.applicationNumber.set(patent.applicationNumber.opt)
    reduced.title.set(patent.title.opt)
    reduced
  }

  def reduceSelf(self: Inventor) = {
    val reduced = new Inventor()
    reduced.inventorID.set(self.inventorID.opt)
    reduced.applicationNumber.set(self.applicationNumber.opt)
    reduced.nameFirst.set(self.nameFirst.opt)
    reduced.nameLast.set(self.nameLast.opt)
    reduced.nameMiddles.set(self.nameMiddles.opt)
    reduced
  }

  def reduceLocation(location: Location) = {
    val reduced = new Location()
    reduced.locationID.set(location.locationID.opt)
    reduced.state.set(location.state.opt)
    reduced.countryCode.set(location.countryCode.opt)
    reduced.countryName.set(location.countryName.opt)
    reduced
  }

  def reduceUSPC(uspc: USPC) = {
    val reduced = new USPC()
    reduced.uuid.set(uspc.uuid.opt)
    reduced.mainclassID.set(uspc.mainclassID.opt)
    reduced.subclassID.set(uspc.subclassID.opt)
    reduced
  }


  /**
    * Reduce all of the mentions in the input json file.
    * Write results to JSON.
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val mentions = LoadJSONInventorMentions.load(new File(args(0)),"UTF-8")
    val reduced = mentions.map(reduce)
    val pw = new PrintWriter(args(1),"UTF-8")
    reduced.foreach{
      case r =>
        pw.println(PatentJsonSerialization.toJsonString(r))
    }
    pw.close()
  }
}
