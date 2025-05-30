package org.realm_war.Models.blocks;

import org.realm_war.Models.Position;

public class VoidBlock extends Block {
    public VoidBlock (Position position) {
        super(position);
    }

    @Override
    public boolean canBuildStructure() {
        return false;
    }

    @Override
    public int getResourceItem(String resourceType) {
        // VoidBlock can't provide anything.
        return 0;
    }
}
