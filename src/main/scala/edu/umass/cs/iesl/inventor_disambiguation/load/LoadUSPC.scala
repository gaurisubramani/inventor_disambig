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

import edu.umass.cs.iesl.inventor_disambiguation.data_structures.classification.USPC

import edu.umass.cs.iesl.inventor_disambiguation._

object LoadUSPC extends TabSeparatedFileLoader[USPC]{
  override def parse(split: Array[String]): Option[USPC] = {
    val cleaned = split.map(_.clean().noneIfEmpty)
    val applicationNumber = split(0).clean().noneIfNAorBlank.get
    val mainClass = split(1).clean()
    val subclassId = split(2).clean()
    Some(new USPC(applicationNumber, mainClass, subclassId))
  }

  override def skipFirstLine: Boolean = true

  override def expectedLineLengths: Set[Int] = Set(4)
}
