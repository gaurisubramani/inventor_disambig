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


package edu.umass.cs.iesl.inventor_disambiguation.load

import edu.umass.cs.iesl.inventor_disambiguation._
import edu.umass.cs.iesl.inventor_disambiguation.data_structures.classification.USPC
import edu.umass.cs.iesl.inventor_disambiguation.data_structures.{Inventor, Location, Patent}


object LoadTDPatent extends CommaSeparateFileLoader[Patent] {
  override def parse(split: Array[String]): Option[Patent] = {
    // patent_number,date,abstract,title,num_claims
    val patent = new Patent()
    patent.applicationNumber.set(split(0))
    patent.title.set(split(3))
    Some(patent)
  }

  override def skipFirstLine: Boolean = true

  override def expectedLineLengths: Set[Int] = Set(5)
}


object LoadTDInventor extends CommaSeparateFileLoader[Inventor] {
  
  override def parse(split: Array[String]): Option[Inventor] = {
    val inventor = new Inventor()
    inventor.applicationNumber.set(split(0).noneIfEmpty)
    inventor.nameFirst.set(split(1).removeQuotes().noneIfEmpty)
    inventor.nameLast.set(split(2).removeQuotes().noneIfEmpty)
    Some(inventor)
  }

  override def skipFirstLine: Boolean = true

  override def expectedLineLengths: Set[Int] = Set(9)
}

object LoadTDClass extends CommaSeparateFileLoader[USPC] {

  private var idx = 0

  override def parse(split: Array[String]): Option[USPC] = {
    //patent_number,mainclass_id,subclass_id
    val uspc = new USPC()
    uspc.uuid.set(idx.toString)
    uspc.applicationNumber.set(split(0))
    uspc.mainclassID.set(split(1))
    uspc.subclassID.set(split(2))
    idx += 1
    Some(uspc)
  }

  override def skipFirstLine: Boolean = true

  override def expectedLineLengths: Set[Int] = Set(3)
}
