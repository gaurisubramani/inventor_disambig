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


package edu.umass.cs.iesl.inventor_disambiguation.data_structures

class Lawyer extends ApplicationViewRecord{

  val uuid = StringSlot("uuid")
  val country = StringSlot("country")
  val nameLine1 = StringSlot("nameLine1")
  val nameLine2 = StringSlot("nameLine2")
  val streetLine1 = StringSlot("streetLine1")
  val streetLine2 = StringSlot("streetLine2")
  val city = StringSlot("city")
  val postalCode = StringSlot("postalCode")
  val regionCode = StringSlot("regionCode")
  val countryCode = StringSlot("countryCode")
  val countryName = StringSlot("countryName")

  def this(uuid: String, applicationNumber: String, nameLine1: String, nameLine2: String, streetLine1: String, streetLine2: String, city: String, postalCode: String, regionCode: String, countryCode: String, countryName: String) = {
    this()
    this.uuid.set(uuid)
    this.applicationNumber.set(applicationNumber)
    this.nameLine1.set(nameLine1)
    this.nameLine2.set(nameLine2)
    this.streetLine1.set(streetLine1)
    this.streetLine2.set(streetLine2)
    this.city.set(city)
    this.postalCode.set(postalCode)
    this.regionCode.set(regionCode)
    this.countryCode.set(countryCode)
    this.countryName.set(countryName)
  }
}

object Lawyer extends Lawyer