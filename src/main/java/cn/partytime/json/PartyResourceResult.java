package cn.partytime.json;

import cn.partytime.model.Party;
import cn.partytime.model.ResourceFile;

import java.util.List;

/**
 * Created by liuwei on 2016/9/12.
 */
public class PartyResourceResult {

    private Party party;

    private List<ResourceFile> resourceFileList;

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public List<ResourceFile> getResourceFileList() {
        return resourceFileList;
    }

    public void setResourceFileList(List<ResourceFile> resourceFileList) {
        this.resourceFileList = resourceFileList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartyResourceResult that = (PartyResourceResult) o;

        if (!party.equals(that.party)) return false;
        return resourceFileList.equals(that.resourceFileList);

    }

    @Override
    public int hashCode() {
        int result = party.hashCode();
        result = 31 * result + resourceFileList.hashCode();
        return result;
    }
}
