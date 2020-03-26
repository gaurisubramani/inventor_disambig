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
import edu.umass.cs.iesl.inventor_disambiguation.data_structures.Lawyer

object LoadLawyer extends TabSeparatedFileLoader[Lawyer]{
  override def parse(split: Array[String]): Option[Lawyer] = {

    val applicationNumber = split(0).clean()
    val nameLine1 = split(1).clean()
    val nameLine2 = split(2).clean()
    val streetLine1 = split(3).clean()
    val streetLine2 = split(4).clean()
    val city = split(5).clean()
    val postalCode = split(6).clean()
    val regionCode = split(7).clean()
    val countryCode = split(8).clean()
    val countryName = split(9).clean()
    val uuid = split(10).clean()

    Some(new Lawyer(uuid, applicationNumber, nameLine1, nameLine2, streetLine1, streetLine2, city, postalCode, regionCode, countryCode, countryName))
  }

  override def skipFirstLine: Boolean = true

  override def expectedLineLengths: Set[Int] = Set(11)
}
