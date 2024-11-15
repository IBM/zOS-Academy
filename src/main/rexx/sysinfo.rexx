/***************** REXX ************************** */
/* SYSINFO                                         */
/*   Date: Nov. 2024                               */
/*                                                 */
/* Sample used in programming workshop.            */
/* That returns some of current system Infos.      */
/*                                                 */
/* STDOUT: String with Current System Infos        */
/* RC: 0  - Success                                */
/*     >0 - Error                                  */
/***************************************************/
rc = 0

curr_user = SYSVAR("SYSUID")
curr_syspref = SYSVAR("SYSPREF")
curr_time = date()

say "Userid       : " curr_user
say "System Prefix: " curr_syspref
say "Time         : " curr_time

part = dayPart( curr_time )
say part

exit rc


/* Function to calculate part of Day */
/*                                   */
/* IN:  Time (String)                */
/*      Format HH:MM:SS              */
/*                                   */
/* OUT: One of following Strings     */
/*      Morning                      */
/*      Afternoon                    */
/*      Night                        */
/*                                   */
dayPart : procedure

    time = arg(1)
    part = "UNKNOWN"
    /* Parse the time into HH:MM:SS */

    parse var time hours ':' minutes ':' seconds

    /* Now Calculate which part of day it is */
    select
       when (hours > 6) & (hours <= 12) then
         part = "Morning"
       when (hours > 12) & (hours <= 18) then
         part = "Afternoon"
       otherwise
         part = "Night"
    end

return part



