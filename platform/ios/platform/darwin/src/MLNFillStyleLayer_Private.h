// This file is generated.
// Edit platform/darwin/scripts/generate-style-code.js, then run `make darwin-style-code`.
#pragma once

#include "MLNStyleLayer_Private.h"

#include <mbgl/layermanager/fill_layer_factory.hpp>

namespace mbgl {

class FillStyleLayerPeerFactory : public LayerPeerFactory, public mbgl::FillLayerFactory {
    // LayerPeerFactory overrides.
    LayerFactory* getCoreLayerFactory() final { return this; }
    virtual MLNStyleLayer* createPeer(style::Layer*) final;
};

}  // namespace mbgl
