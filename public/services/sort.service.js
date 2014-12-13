/**
 * Created by Eric on 10/12/2014.
 */

angular
    .module("NourritureApp")
    .factory("SortService", SortService);

function SortService() {
    var service = {
        roleAsc: roleAsc,
        roleDesc: roleDesc,
        nameAsc: nameAsc,
        nameDesc: nameDesc,
        creatorAsc: creatorAsc,
        creatorDesc: creatorDesc
    };

    return service;

    function roleAsc(a, b) {
        return a.role.localeCompare(b.role);
    }

    function roleDesc(a, b) {
        return b.role.localeCompare(a.role);
    }

    function nameAsc(a, b) {
        return a.name.localeCompare(b.name);
    }

    function nameDesc(a, b) {
        return b.name.localeCompare(a.name);
    }

    function creatorAsc(a, b) {
        return a.creator.localeCompare(b.creator);
    }

    function creatorDesc(a, b) {
        return b.creator.localeCompare(a.creator);
    }
}